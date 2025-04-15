package org.example.mapper;

public interface Mapper <D,E>{
    public D toDto (E entity);
    public E toEntity (D dto);
    public E updateToEntity (D dto , E entity);

}
