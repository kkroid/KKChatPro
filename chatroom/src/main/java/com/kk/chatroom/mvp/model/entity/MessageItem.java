package com.kk.chatroom.mvp.model.entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.converter.PropertyConverter;

@Entity
public class MessageItem {
    @Id
    public long id;
    public String msgId;
    public String text;
    public String sender;
    public String sendTo;
    public long time;
    @Convert(converter = MsgTypeConverter.class, dbType = Integer.class)
    public MsgType type;
    @Convert(converter = MsgStatusConverter.class, dbType = Integer.class)
    public MsgStatus status;
    @Convert(converter = ExtraConverter.class, dbType = String.class)
    public Map<String, String> extra;

    public static class MsgTypeConverter implements PropertyConverter<MsgType, Integer> {

        @Override
        public MsgType convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return MsgType.Text;
            }
            return MsgType.get(databaseValue);
        }

        @Override
        public Integer convertToDatabaseValue(MsgType entityProperty) {
            return entityProperty == null ? MsgType.Text.id : entityProperty.id;
        }
    }

    public static class MsgStatusConverter implements PropertyConverter<MsgStatus, Integer> {

        @Override
        public MsgStatus convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return MsgStatus.Sent;
            }
            return MsgStatus.get(databaseValue);
        }

        @Override
        public Integer convertToDatabaseValue(MsgStatus entityProperty) {
            return entityProperty == null ? MsgStatus.Sent.id : entityProperty.id;
        }
    }

    public static class ExtraConverter implements PropertyConverter<Map<String, String>, String> {

        @Override
        public Map<String, String> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null || databaseValue.trim().length() == 0) {
                return null;
            }
            try {
                Map<String, String> extra = new HashMap<>();
                JSONObject jsonObject = new JSONObject(databaseValue);
                Iterator<String> iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String value = jsonObject.getString(key);
                    extra.put(key, value);
                }
                return extra;
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public String convertToDatabaseValue(Map<String, String> entityProperty) {
            if (entityProperty == null || entityProperty.keySet().isEmpty()) {
                return null;
            }
            JSONObject jsonObject = new JSONObject();
            for (String key : entityProperty.keySet()) {
                try {
                    jsonObject.put(key, entityProperty.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonObject.toString();
        }
    }
}
