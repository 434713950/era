/*
 * Copyright (C) 2025  ChengPeng
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.ourexists.era.framework.orm.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.ourexists.era.framework.core.constants.CommonConstant;
import com.ourexists.era.framework.core.user.*;
import org.bson.Document;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoWriter;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author pengcheng
 * @date 2022/7/29 10:51
 * @since 1.0.0
 */
public class EraMongoTemplate extends MongoTemplate {

    private static final String TENANT_FIELD_NAME = "tenantId";


    public EraMongoTemplate(MongoClient mongoClient, String databaseName) {
        super(mongoClient, databaseName);
    }

    public EraMongoTemplate(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    public EraMongoTemplate(MongoDatabaseFactory mongoDbFactory, MongoConverter mongoConverter) {
        super(mongoDbFactory, mongoConverter);
    }

    @Override
    public <T> List<T> find(Query query, Class<T> entityClass, String collectionName) {
        return super.find(tenantCriteriaHandle(query, entityClass, OperatorModel.QUERY), entityClass, collectionName);
    }

    @Override
    public <T> List<T> findDistinct(Query query, String field, String collectionName, Class<?> entityClass,
                                    Class<T> resultClass) {
        return super.findDistinct(tenantCriteriaHandle(query, entityClass, OperatorModel.QUERY), field, collectionName, entityClass, resultClass);
    }

    @Override
    protected <T> List<T> doFindAndDelete(String collectionName, Query query, Class<T> entityClass) {
        return super.doFindAndDelete(collectionName, tenantCriteriaHandle(query, entityClass, OperatorModel.DELETE), entityClass);
    }

    @Override
    protected <T> T doFindAndRemove(CollectionPreparer collectionPreparer, String collectionName, Document query, Document fields, Document sort, Collation collation, Class<T> entityClass) {
        return super.doFindAndRemove(collectionPreparer, collectionName, tenantCriteriaHandle(query, entityClass, OperatorModel.DELETE), fields, sort, collation, entityClass);
    }

    @Override
    protected <T> T doFindAndModify(CollectionPreparer collectionPreparer, String collectionName, Document query, Document fields, Document sort, Class<T> entityClass, UpdateDefinition update, FindAndModifyOptions options) {
        updateCommonHandle(update.getUpdateObject());
        return super.doFindAndModify(collectionPreparer, collectionName, tenantCriteriaHandle(query, entityClass, OperatorModel.UPDATE), fields, sort, entityClass, update, options);
    }

    @Override
    protected <T> T doFindAndReplace(CollectionPreparer collectionPreparer, String collectionName, Document mappedQuery, Document mappedFields, Document mappedSort, com.mongodb.client.model.Collation collation, Class<?> entityType, Document replacement, FindAndReplaceOptions options, Class<T> resultType) {
        return super.doFindAndReplace(collectionPreparer, collectionName, tenantCriteriaHandle(mappedQuery, entityType, OperatorModel.UPDATE), mappedFields, mappedSort, collation, entityType, replacement, options, resultType);
    }

    @Override
    protected <T> T doFindOne(String collectionName, CollectionPreparer<MongoCollection<Document>> collectionPreparer, Document query, Document fields, CursorPreparer preparer, Class<T> entityClass) {
        return super.doFindOne(collectionName, collectionPreparer, tenantCriteriaHandle(query, entityClass, OperatorModel.QUERY), fields, preparer, entityClass);
    }


    @Override
    public long count(Query query, @Nullable Class<?> entityClass, String collectionName) {
        return super.count(tenantCriteriaHandle(query, entityClass, OperatorModel.QUERY), entityClass, collectionName);
    }

    @Override
    protected <T> T doSave(String collectionName, T objectToSave, MongoWriter<T> writer) {
        fillTenantId(objectToSave);
        fillCommonField(objectToSave, false);
        return super.doSave(collectionName, objectToSave, writer);
    }

    @Override
    protected <T> T doInsert(String collectionName, T objectToSave, MongoWriter<T> writer) {
        fillTenantId(objectToSave);
        fillCommonField(objectToSave, true);
        return super.doInsert(collectionName, objectToSave, writer);
    }

    @Override
    protected <T> Collection<T> doInsertAll(Collection<? extends T> listToSave, MongoWriter<T> writer) {
        for (T t : listToSave) {
            fillTenantId(t);
            fillCommonField(t, true);
        }
        return super.doInsertAll(listToSave, writer);
    }

    @Override
    protected <T> Collection<T> doInsertBatch(String collectionName, Collection<? extends T> batchToSave, MongoWriter<T> writer) {
        for (T t : batchToSave) {
            fillTenantId(t);
            fillCommonField(t, true);
        }
        return super.doInsertBatch(collectionName, batchToSave, writer);
    }

    @Override
    protected <T> DeleteResult doRemove(String collectionName, Query query, Class<T> entityClass, boolean multi) {
        return super.doRemove(collectionName, tenantCriteriaHandle(query, entityClass, OperatorModel.DELETE), entityClass, multi);
    }

    @Override
    protected UpdateResult doUpdate(String collectionName, Query query, UpdateDefinition update, @Nullable Class<?> entityClass, boolean upsert, boolean multi) {
        updateCommonHandle(update.getUpdateObject());
        return super.doUpdate(collectionName, tenantCriteriaHandle(query, entityClass, OperatorModel.UPDATE), update, entityClass, upsert, multi);
    }

    private void updateCommonHandle(Document update) {
        UserInfo userInfo = UserContext.getUser();
        if (userInfo != null) {
            update.append("updatedId", userInfo.getId())
                    .append("updatedBy", userInfo.getUsername());

        }
        update.append("updatedTime", new Date());
    }

    private <T> void fillTenantId(T objectToSave) {
        String tenantId = null;
        TenantInfo tenantInfo = UserContext.getTenant();
        if (tenantInfo != null) {
            tenantId = tenantInfo.getTenantId();
        }
        fillField(objectToSave, TENANT_FIELD_NAME, tenantId);
    }

    private <T> void fillCommonField(T objectToSave, boolean isInsert) {
        UserInfo userInfo = UserContext.getUser();
        if (userInfo != null) {
            if (isInsert) {
                fillField(objectToSave, "createdBy", userInfo.getUsername());
                fillField(objectToSave, "createdId", userInfo.getId());
            }
            fillField(objectToSave, "updatedBy", userInfo.getUsername());
            fillField(objectToSave, "updatedId", userInfo.getId());
        }
        if (isInsert) {
            fillField(objectToSave, "createdTime", new Date());
        }
        fillField(objectToSave, "updatedTime", new Date());
    }

    private <T> void fillField(T objectToSave, String fieldName, Object val) {
        if (val == null) {
            return;
        }
        Field field = getDeclaredField(objectToSave.getClass(), fieldName);
        if (field == null) {
            return;
        }
        try {
            field.setAccessible(true);
            Object source = field.get(objectToSave);
            if (source == null) {
                field.set(objectToSave, val);
            }
        } catch (IllegalAccessException e) {
            //nothing
        } finally {
            field.setAccessible(false);
        }
    }

    private Field getDeclaredField(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class sc = clazz.getSuperclass();
            if (sc == null) {
                return null;
            }
            return getDeclaredField(sc, fieldName);
        }
    }


    private Criteria tenantCriteriaHandle(Criteria criteria, Class<?> entityType, OperatorModel operatorModel) {
        String tenantIdName = getTenantIdName(entityType);
        Object o = criteria.getCriteriaObject().get(tenantIdName);
        if (o != null) {
            String val = (String) o;
            //tenantId为0时去除条件
            if (val.equals(CommonConstant.SYSTEM_TENANT)) {
                criteria.getCriteriaObject().remove(tenantIdName);
            }
            return criteria;
        }
        if (needfroce(entityType)) {
            if (EraDataAccessAuth.checkTenantControlPower(operatorModel)) {
                Pattern pattern = Pattern.compile("^" + UserContext.getTenant().getTenantId() + "\\d*$", Pattern.CASE_INSENSITIVE);
                criteria.and(tenantIdName).regex(pattern);
            } else {
                criteria.and(tenantIdName).is(UserContext.getTenant().getTenantId());
            }
        }
        return criteria;
    }


    private Document tenantCriteriaHandle(Document query, Class<?> entityType, OperatorModel operatorModel) {
        String tenantIdName = getTenantIdName(entityType);
        Object o = query.get(tenantIdName);
        if (o != null) {
            String val = (String) o;
            //tenantId为0时去除条件
            if (val.equals(CommonConstant.SYSTEM_TENANT)) {
                query.remove(tenantIdName);
            }
            return query;
        }
        if (needfroce(entityType)) {
            if (EraDataAccessAuth.checkTenantControlPower(operatorModel)) {
                Pattern pattern = Pattern.compile("^" + UserContext.getTenant().getTenantId() + "\\d*$", Pattern.CASE_INSENSITIVE);
                query.append(tenantIdName, pattern);
            } else {
                query.append(tenantIdName, UserContext.getTenant().getTenantId());
            }
        }
        return query;
    }

    private Query tenantCriteriaHandle(Query query, Class<?> entityType, OperatorModel operatorModel) {
        String tenantIdName = getTenantIdName(entityType);
        Object o = query.getQueryObject().get(tenantIdName);
        if (o != null) {
            if (o instanceof String val) {
                //tenantId为0时去除条件
                if (val.equals(CommonConstant.SYSTEM_TENANT)) {
                    query.getQueryObject().remove(tenantIdName);
                }
            }
            return query;
        }
        if (needfroce(entityType)) {
            if (EraDataAccessAuth.checkTenantControlPower(operatorModel)) {
                Pattern pattern = Pattern.compile("^" + UserContext.getTenant().getTenantId() + "\\d*$", Pattern.CASE_INSENSITIVE);
                query.addCriteria(Criteria.where(tenantIdName).regex(pattern));
            } else {
                query.addCriteria(Criteria.where(tenantIdName).is(UserContext.getTenant().getTenantId()));
            }
        }
        return query;
    }

    private String getTenantIdName(Class<?> entityType) {
        Field field = getDeclaredField(entityType, TENANT_FIELD_NAME);
        if (field != null) {
            org.springframework.data.mongodb.core.mapping.Field t =
                    field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
            if (t != null) {
                return t.value();
            }
        }
        return TENANT_FIELD_NAME;
    }

    private boolean needfroce(Class<?> entityType) {
        String tenantId = UserContext.getTenant().getTenantId();
        if (tenantId.equals(CommonConstant.SYSTEM_TENANT)) {
            return false;
        }
        return getDeclaredField(entityType, TENANT_FIELD_NAME) != null;
    }
}
