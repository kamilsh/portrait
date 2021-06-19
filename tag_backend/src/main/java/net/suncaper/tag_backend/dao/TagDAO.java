package net.suncaper.tag_backend.dao;

import net.suncaper.tag_backend.pojo.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagDAO extends JpaRepository<Tag, Integer> {
    List<Object[]> getAllByPid(int pid);
    List<Tag> findAllByPid(int pid);
    Tag getTagById(int id);
}
