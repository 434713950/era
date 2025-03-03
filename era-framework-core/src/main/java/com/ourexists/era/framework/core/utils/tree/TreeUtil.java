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

package com.ourexists.era.framework.core.utils.tree;


import com.ourexists.era.framework.core.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>树形结构工具/p>
 *
 * @author PengCheng
 * @date 2018/10/31
 */
public class TreeUtil {

    public static final String PPID_SPLIT = ";";

    public static final String ROOT_CODE = "0";

    public static String generateCode(String pcode, String otherMaxCode) {
        if (otherMaxCode == null) {
            String pre = pcode;
            if (pcode.equals(ROOT_CODE)) {
                pre = "";
            }
            return pre + "001";
        } else {
            int num = otherMaxCode.length();
            return String.format("%0" + num, Integer.parseInt(otherMaxCode) + 1);
        }
    }

    /**
     * 挂载子节点,形成树结构 （所有可挂载的子节点已确定）
     * 递归处理
     *
     * @param pNode        父节点,最终所有数据都会存在pNode下
     * @param treeNodeList 所有与该根关联的下级节点
     */
    public static <T extends TreeNode<T>> void mountChildrenNode(T pNode, List<T> treeNodeList) {
        //找出所有的子节点
        List<T> children = new ArrayList<>();
        for (T child : treeNodeList) {
            //递归形成子节点
            if (null != child.getPcode() && child.getPcode().equals(pNode.getCode())) {
                //递归挂载子节点
                mountChildrenNode(child, treeNodeList);
                children.add(child);
            }
        }
        //节点挂载
        pNode.setChildren(children);
    }

    /**
     * 铺平树结构
     *
     * @param isRetainChildren 是否要在铺平树结构后,继续展示各自的子节点数据
     * @return 铺平后的树
     */
    public static <T extends TreeNode<T>> List<T> tilingTreeNodes(T pNode, boolean isRetainChildren) {
        List<T> storageContainer = new LinkedList<>();
        openChildrenNode(pNode, storageContainer, isRetainChildren);
        return storageContainer;
    }

    /**
     * 从非成树的结构组织中提取一个节点下所有关联的子节点（包含自己）
     *
     * @param treeNodes 所有的节点数据
     * @param code      要查询的节点
     * @param <T>
     * @return
     */
    public static <T extends TreeNode<T>> List<T> extractAllChildrenNode(List<T> treeNodes, String code) {
        if (CollectionUtil.isBlank(treeNodes)) {
            return null;
        }
        List<T> children = new ArrayList<>();
        extractChildrenNode(treeNodes, code, children);
        return children;
    }

    /**
     * 提取所有与传入节点相关的下层节点
     *
     * @param treeNodes   所有的数据
     * @param code         节点code
     * @param container   存储数据的容器
     * @param <T>rootNode
     */
    private static <T extends TreeNode<T>> void extractChildrenNode(List<T> treeNodes, String code, List<T> container) {
        for (T treeNode : treeNodes) {
            if (treeNode.getPcode().equals(code)) {
                container.add(treeNode);
                extractChildrenNode(treeNodes, treeNode.getCode(), container);
            }
        }
    }


    /**
     * 铺平树结构
     *
     * @param pNode             父节点
     * @param storageContainer  存储容器
     * @param isRetainChildren  是否展示子节点信息
     * @param <T>
     */
    private static <T extends TreeNode<T>> void openChildrenNode(T pNode, List<T> storageContainer, boolean isRetainChildren) {
        //取出子节点数据
        List<T> childrenNode = pNode.getChildren();
        //判断是否要保留子节点数据做展示
        if (!isRetainChildren) {
            pNode.setChildren(null);
        }
        storageContainer.add(pNode);
        if (CollectionUtil.isNotBlank(childrenNode)) {
            for (T childNode : childrenNode) {
                openChildrenNode(childNode, storageContainer, isRetainChildren);
            }
        }
    }

    /**
     * 取指定节点树结构关系
     *
     * @param treeNodeList 所有节点数据
     * @param <T>
     * @return
     */
    public static <T extends TreeNode<T>> List<T> getTreeRelationById(List<T> treeNodeList, String code) {
        List<T> container = new ArrayList<>();
        if (CollectionUtil.isBlank(treeNodeList)) {
            return null;
        }
        getChildTree(treeNodeList, code, container);
        return container;
    }

    private static <T extends TreeNode<T>> void getChildTree(List<T> treeNodeList, String code, List<T> container) {
        for (T pNode : treeNodeList) {
            if (pNode.getCode().equals(code)) {
                container.add(pNode);
                getChildTree(treeNodeList, pNode.getPcode(), container);
            }
        }
    }


    /**
     * 提取出根节点（未折叠）有几个取几个
     *
     * @param treeNodeList 节点数据
     * @param <T>
     * @return
     */
    public static <T extends TreeNode<T>> List<T> extractAllRootNode(List<T> treeNodeList) {
        List<T> roots = new ArrayList<>();
        for (T treeNode : treeNodeList) {
            if (null == treeNode.getPcode() || treeNode.getPcode().equals(ROOT_CODE)) {
                roots.add(treeNode);
            }
        }
        return roots;
    }

    /**
     * 折叠出根节点树结构
     *
     * @param treeNodeList 所有节点数据
     * @param <T>
     * @return
     */
    public static <T extends TreeNode<T>> List<T> foldRootTree(List<T> treeNodeList) {
        List<T> treeNodes = extractAllRootNode(treeNodeList);
        if (CollectionUtil.isNotBlank(treeNodes)) {
            treeNodes.forEach(
                    treeNode -> mountChildrenNode(treeNode, treeNodeList)
            );
        }
        return treeNodes;
    }

}
