package com.hq.myblog.Service;

import com.hq.myblog.Dao.TypeRepository;
import com.hq.myblog.Exception.CustomException;
import com.hq.myblog.Exception.CustomExceptionType;
import com.hq.myblog.Po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeRepository typeRepository;

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Transactional
    @Override
    public Type findByName(String name) {
        return typeRepository.findByName(name);
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort.Order order = new Sort.Order(Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, Sort.by(order));
        return typeRepository.findTop(pageable);

    }

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Type UpdateType(Long id, Type type) {
        Type t = getType(id);
        if (t == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, id + "号类型不存在");
        }
        BeanUtils.copyProperties(type, t);
        return typeRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }
}
