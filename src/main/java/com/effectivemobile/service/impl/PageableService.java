package com.effectivemobile.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PageableService {

    public Pageable getPageable(Integer offset, Integer limit) {
        return PageRequest.of(offset, limit);
    }
}
