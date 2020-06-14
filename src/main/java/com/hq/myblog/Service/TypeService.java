package com.hq.myblog.Service;

import com.hq.myblog.Po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TypeService {
    Type saveType(Type type);

    Type getType(Long id);

    Page<Type> listType(Pageable pageable);

    Type UpdateType(Long id, Type type);

    List<Type> listTypeTop(Integer size);

    void deleteType(Long id);

    Type findByName(String name);

    List<Type> listType();


}
