package net.suncaper.tag_backend.hbase.utils;

import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NamespaceUtil {
    public static List<String> listNamespace(Connection connection) throws IOException {
        List<String> nss = new ArrayList<>();
        Admin admin = connection.getAdmin();

        NamespaceDescriptor[] namespaceDescriptors = admin.listNamespaceDescriptors();
        for (NamespaceDescriptor namespaceDescriptor: namespaceDescriptors) {
            nss.add(namespaceDescriptor.getName());
        }
        admin.close();
        return nss;
    }
}
