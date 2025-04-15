package com.example.questions_5;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;

import java.io.File;

public class FileUtil {
    public static String getFullPathFromTreeUri(final Uri treeUri, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String docId = DocumentsContract.getTreeDocumentId(treeUri);
            String[] split = docId.split(":");
            String type = split[0];
            String relativePath = split.length > 1 ? split[1] : "";

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + relativePath;
            }
        }
        return null;
    }
}
