package com.devfiles.core.file.domain;

import com.devfiles.core.user.domain.User;
import com.devfiles.enterprise.domain.entity.BaseDomain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
public class File extends BaseDomain {
    private String name;
    private String path;
    private String mimeType;
    private Long size;
    private User user;

    public String getNameWithExtension() {
        return name + "." + mimeType.split("/")[1];
    }
}
