package com.ourexists.era.framework.excel;

import com.ourexists.era.framework.oss.UploadPartR;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collection;

@Getter
@Setter
@Accessors(chain = true)
public class ExportPartDataResult {

    private Integer partNum;

    private Boolean isEnd = false;

    private String uploadId;

    private String fileName;

    private UploadPartR uploadPartR;

    /**
     * 导出包含的列
     */
    private Collection<Integer> includeColumnIndexes;

}
