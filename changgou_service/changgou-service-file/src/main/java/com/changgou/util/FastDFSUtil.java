package com.changgou.util;

import com.changgou.file.FastDFSFile;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 文件上传管理类
 * 1.文件上传
 * 2.文件删除
 * 3.文件下载
 * 4.文件信息获取
 * 5.storage信息获取
 * 6.Tracker信息获取
 */

public class FastDFSUtil {

    /**
     * 加载Tracker链接信息
     */
    static {
        try {
            String fileName = new ClassPathResource("fdfs_client.conf").getPath();
            ClientGlobal.init(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     * @param fastDFSFile
     */

    public static String[] upload(FastDFSFile fastDFSFile) throws Exception{

        StorageClient storageClient = getStorageClient();

        /**
         * 1.上传文件的字节数组
         * 2.文件的扩展名
         * 3.附加参数
         */
        /**
         * uploads 参数:
         * 1.upload[0] 文件上传所存储的Storage的组名称 group1
         * 2.upload[1] 文件存储到Storage的文件名字, M00/00/44/xxx.jpg
         */
       String [] uploads = storageClient.upload_file(fastDFSFile.getContent(),fastDFSFile.getExt(),null);

       return uploads;
    }

    /**
     * 获取文件信息
     * @param groupName: 文件的组名
     * @param remoteFileName :文件存储的文件名
     */

    public static FileInfo getFileInfo(String groupName, String remoteFileName) throws Exception {

        //获取Storage对象
        StorageClient storageClient = getStorageClient();

        FileInfo file_info = storageClient.get_file_info(groupName, remoteFileName);

        return file_info;
    }

    /**
     * 文件下载
     * @param groupName
     * @param remoteFileName
     */
    public static InputStream downloadFile(String groupName, String remoteFileName) throws Exception {

        StorageClient storageClient = getStorageClient();

        //文件下载
        byte[] bytes = storageClient.download_file(groupName, remoteFileName);

        return new ByteArrayInputStream(bytes);
    }

    /**
     * 删除文件
     * @param groupName
     * @param remoteFileName
     */
    public static void deleteFile(String groupName, String remoteFileName) throws Exception {
        StorageClient storageClient = getStorageClient();
        storageClient.delete_file(groupName,remoteFileName);
    }

    /**
     * 获取Storage的存储对象
     * @return
     * @throws Exception
     */
    private static StorageClient getStorageClient () throws Exception {
        TrackerServer trackerServer = getTrackerServer();
        //通过TrackerServer的链接信息可以获取Storage的链接信息, 创建StorageClient对象存储Storage的链接信息
        StorageClient storageClient = new StorageClient(trackerServer, null);
        //通过StorageClient访问Storage 实现文件上传, 并且获取文件上传后的存储信息
        return storageClient;
    }

    /**
     * 获取TrackerServer
     */
    private static TrackerServer getTrackerServer() throws Exception {
        //创建一个Tracker访问的客户端对象 TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        //通过 TrackerClient 访问 TrackerServer服务, 获取链接信息
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }

    /**
     * 获取Storage信息
     */
    public static StorageServer getStorageServer() throws Exception {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer    = trackerClient.getConnection();
        return trackerClient.getStoreStorage(trackerServer);
    }

    /**
     * 获取Storage的IP和端口信息
     * @param groupName
     * @parma remoteFileName
     */
    public static ServerInfo[] getServerInfo(String groupName, String remoteFileName) throws Exception {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getFetchStorages(trackerServer,groupName,remoteFileName);
    }

    /**
     * 获取Tracker的信息
     */
    public static String getTrackerInfo() throws Exception {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        //Tracker的IP和HTTP端口号
        int g_tracker_http_port = ClientGlobal.getG_tracker_http_port();//获取端口号
        String hostString = trackerServer.getInetSocketAddress().getHostString();//获取主机名称
        String path = "http://"+hostString+g_tracker_http_port;
        return path;
    }
}


