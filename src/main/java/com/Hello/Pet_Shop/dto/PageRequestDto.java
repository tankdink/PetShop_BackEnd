package com.Hello.Pet_Shop.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageRequestDto
{
    private Integer pageNo = 0;

    private Integer pageSize = 5;

    public Pageable getPageable(Integer pageNo, Integer pageSize)
    {
        Integer page = Objects.nonNull(pageNo) ? pageNo : this.pageNo;
        Integer size = Objects.nonNull(pageSize) ? pageSize : this.pageSize;

        PageRequest request = PageRequest.of(page, size, Sort.by("id").ascending());

        return request;
    }

}
