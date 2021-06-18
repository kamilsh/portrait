package net.suncaper.tag_backend.service;

import net.suncaper.tag_backend.dao.TagDAO;
import net.suncaper.tag_backend.pojo.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    TagDAO tagDAO;

    public List<Object[]> getTagsByPid(int pid) {
        return tagDAO.getAllByPid(pid);
    }

    public List<Tag> findTagsByPid(int pid) {
        return tagDAO.findAllByPid(pid);
    }


}
