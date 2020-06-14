package com.hq.myblog.Service;

import com.hq.myblog.Dao.TagRepository;
import com.hq.myblog.Exception.CustomException;
import com.hq.myblog.Exception.CustomExceptionType;
import com.hq.myblog.Po.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> listTag(String tagsId) {
        return tagRepository.findAllById(convertTags(tagsId));
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.findById(id).get();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, Sort.by(order));
        return tagRepository.findTop(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    public static List<Long> convertTags(String tags) {
        List<Long> list = new ArrayList();
        if (!"".equals(tags) && tags != null) {
            String[] ids = tags.split(",");
            for (String id : ids) {
                list.add(Long.valueOf(id));
            }
        }
        return list;
    }

    @Transactional
    @Override
    public Tag UpdateTag(Long id, Tag tag) {
        Tag t = getTag(id);
        if (t == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, id + "号标签不存在");
        }
        BeanUtils.copyProperties(tag, t);
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Tag findByName(String name) {
        return tagRepository.findByName(name);
    }
}
