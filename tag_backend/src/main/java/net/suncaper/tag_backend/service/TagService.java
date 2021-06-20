package net.suncaper.tag_backend.service;

import com.alibaba.fastjson.JSONObject;
import net.suncaper.tag_backend.dao.TagDAO;
import net.suncaper.tag_backend.pojo.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    @Autowired
    TagDAO tagDAO;

    private static final int PERSONALPID = 3;
    private static final int BUSINESSPID = 4;
    private static final int ACTIONPID = 5;

    public List<Object[]> getTagsByPid(int pid) {
        return tagDAO.getAllByPid(pid);
    }

    public List<Tag> findTagsByPid(int pid) {
        return tagDAO.findAllByPid(pid);
    }

    public List<JSONObject> getCombinationTags() {
        List<Tag> personalTags = tagDAO.findAllByPid(PERSONALPID);
        List<Tag> businessTags = tagDAO.findAllByPid(BUSINESSPID);
        List<Tag> actionTags = tagDAO.findAllByPid(ACTIONPID);

        List<Tag> newBusinessTags = new ArrayList<>();
        for (Tag businessTag : businessTags) {
            if ("客单价".equals(businessTag.getName()) || "单笔最高".equals(businessTag.getName()))
                continue;
            newBusinessTags.add(businessTag);
        }

        List<Tag> newActionTags = new ArrayList<>();
        for (Tag tag : actionTags) {
            if ("浏览页面".equals(tag.getName()) || "设备类型".equals(tag.getName()))
                continue;
            newActionTags.add(tag);
        }

        JSONObject personalOption = getAttributeByTag(personalTags, PERSONALPID, "人口属性");
        JSONObject businessOption = getAttributeByTag(newBusinessTags, BUSINESSPID, "商业属性");
        JSONObject actionOption = getAttributeByTag(newActionTags, ACTIONPID, "行为属性");

        List<JSONObject> jsonObjectList = new ArrayList<>();
        jsonObjectList.add(personalOption);
        jsonObjectList.add(businessOption);
        jsonObjectList.add(actionOption);

        return jsonObjectList;
    }

    public JSONObject getAttributeByTag(List<Tag> tags, int tagValue, String tagLabel) {
        JSONObject jsonObjectFeature = new JSONObject();
        jsonObjectFeature.put("value", tagValue);
        jsonObjectFeature.put("label", tagLabel);
        List<JSONObject> jsonObjectFeatureList = new ArrayList<>();
        for (Tag tag : tags) {
            int id = tag.getId();
            String name = tag.getName();
            JSONObject jsonObjectTag = new JSONObject();
            jsonObjectTag.put("value", id);
            jsonObjectTag.put("label", name);
            List<JSONObject> jsonObjectAttributeList = new ArrayList<>();
            List<Tag> attributeTags = tagDAO.findAllByPid(id);
            for (Tag attributeTag : attributeTags) {
                int value = attributeTag.getId();
                String label = attributeTag.getName();
                JSONObject jsonObjectAttribute = new JSONObject();
                jsonObjectAttribute.put("value", value);
                jsonObjectAttribute.put("label", label);
                jsonObjectAttributeList.add(jsonObjectAttribute);
            }
            jsonObjectTag.put("children", jsonObjectAttributeList);
            jsonObjectFeatureList.add(jsonObjectTag);
        }
        jsonObjectFeature.put("children", jsonObjectFeatureList);
        return jsonObjectFeature;
    }

    public List<String> getRemarkByID(List<Integer> IDList) {
        List<String> remarkList = new ArrayList<>();
        for (Integer id : IDList) {
            remarkList.add(tagDAO.getTagById(id).getRemark());
        }
        return remarkList;
    }

    public List<String> getNameByID(List<Integer> IDList) {
        List<String> nameList = new ArrayList<>();
        for (Integer id : IDList) {
            nameList.add(tagDAO.getTagById(id).getName());
        }
        return nameList;
    }

}
