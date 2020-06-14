package com.hq.myblog.Service;

import com.hq.myblog.Po.Tag;
import com.hq.myblog.Po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface TagService {
    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Page<Tag> listTag(Pageable pageable);

    Tag UpdateTag(Long id, Tag tag);

    void deleteTag(Long id);

    Tag findByName(String name);

    List<Tag> listTag();

    List<Tag> listTagTop(Integer size);

    List<Tag> listTag(String tagsId);
}
