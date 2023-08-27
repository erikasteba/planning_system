package com.example.planning_system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "images")
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Getter
    private String name;
    @Getter
    private String originalFileName;
    @Getter
    private Long size;
    @Getter
    private String contentType;
    private boolean isPreviewImage;
    @Getter
    @Lob
    private byte[] bytes;

    @Getter
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private User user;

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isPreviewImage() {
        return isPreviewImage;
    }

    public void setPreviewImage(boolean previewImage) {
        isPreviewImage = previewImage;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
