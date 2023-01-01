package cn.hellohao.service.impl

@Service
class ImgServiceImpl : ServiceImpl<ImgMapper?, Images?>(), ImgService {
    @Resource
    private val imgMapper: ImgMapper? = null
    override fun selectImages(imgSearchDto: ImgSearchDto?): List<Images?> {
        return imgMapper.selectImageData(imgSearchDto)
    }

    override fun deleteImgById(id: String?): Int {
        return imgMapper.deleteById(id)
    }

    override fun deleimgForImgUid(imguid: String?): Int {
        return imgMapper.deleimgForImgUid(imguid)
    }

    override fun selectByPrimaryKey(id: String?): Images {
        return imgMapper.selectByPrimaryKey(id)
    }

    //删除对象存储的图片文件
    fun delect(key: StorageKey, fileName: String?) {
        // 初始化
        val credentials: Credentials = BasicCredentials(key.accessKey, key.accessSecret)
        val nosClient = NosClient(credentials)
        nosClient.setEndpoint(key.endpoint)
        // 初始化TransferManager
        val transferManager: TransferManager = TransferManager(nosClient)
        //列举桶
        val bucketList: ArrayList<*> = ArrayList<Any?>()
        var tname = ""
        for (bucket in nosClient.listBuckets()) {
            bucketList.add(bucket.name)
        }
        for (`object` in bucketList) {
            tname = `object`.toString()
            //查看桶的ACL
            val acl: CannedAccessControlList = nosClient.getBucketAcl(`object`.toString())
        }
        //这种方法不能删除指定文件夹下的文件
        val isExist: Boolean = nosClient.doesObjectExist(tname, fileName, null)
        println("文件是否存在：$isExist")
        if (isExist) {
            nosClient.deleteObject(tname, fileName)
        }
    }

    fun delectOSS(key: StorageKey, fileName: String) {
        val endpoint = key.endpoint
        val accessKeyId = key.accessKey
        val accessKeySecret = key.accessSecret
        val bucketName = key.bucketName
        val ossClient = OSSClient(endpoint, accessKeyId, accessKeySecret)
        ossClient.deleteObject(bucketName, fileName)
        ossClient.shutdown()
    }

    //删除USS对象存储的图片文件
    fun delectUSS(key: StorageKey, fileName: String?) {
        val upyun = UpYun(key.bucketName, key.accessKey, key.accessSecret)
        try {
            val result: Boolean = upyun.deleteFile(fileName, null)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: UpException) {
            e.printStackTrace()
        }
    }

    fun delectUFile(key: StorageKey, fileName: String?) {
        val upyun = UpYun(key.bucketName, key.accessKey, key.accessSecret)
        try {
            val result: Boolean = upyun.deleteFile(fileName, null)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: UpException) {
            e.printStackTrace()
        }
    }

    fun delectKODO(key: StorageKey, fileName: String?) {
        val cfg: Configuration
        cfg = if (key.endpoint == "1") {
            Configuration(Zone.zone0())
        } else if (key.endpoint == "2") {
            Configuration(Zone.zone1())
        } else if (key.endpoint == "3") {
            Configuration(Zone.zone2())
        } else if (key.endpoint == "4") {
            Configuration(Zone.zoneNa0())
        } else {
            Configuration(Zone.zoneAs0())
        }
        val auth: Auth = Auth.create(key.accessKey, key.accessSecret)
        val bucketManager = BucketManager(auth, cfg)
        try {
            bucketManager.delete(key.bucketName, fileName)
        } catch (ex: QiniuException) {

            //如果遇到异常，说明删除失败
            System.err.println(ex.code())
            System.err.println(ex.response.toString())
        }
    }

    //删除COS对象存储的图片文件
    fun delectCOS(key: StorageKey, fileName: String) {
        val cred: COSCredentials = BasicCOSCredentials(key.accessKey, key.accessSecret)
        val region = Region(key.endpoint)
        val clientConfig = ClientConfig(region)
        val cosClient = COSClient(cred, clientConfig)
        try {
            val bucketName = key.bucketName
            cosClient.deleteObject(key.bucketName, fileName)
        } catch (serverException: CosServiceException) {
            serverException.printStackTrace()
        } catch (clientException: CosClientException) {
            clientException.printStackTrace()
        }
    }

    fun delectFTP(key: StorageKey, fileName: String?) {
        val ftp = FTPClient()
        val host = key.endpoint.split("\\:".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val h = host[0]
        val p = host[1].toInt()
        try {
            if (!ftp.isConnected()) {
                ftp.connect(h, p)
            }
            ftp.login(key.accessKey, key.accessSecret)
            ftp.deleteFile(fileName)
        } catch (e: IOException) {
            e.printStackTrace()
            Print.warning("删除FTP存储的图片失败")
        }
    }

    override fun counts(userid: String?): Int {
        // TODO Auto-generated method stub
        return imgMapper.counts(userid)
    }

    override fun countimg(userid: String?): Int {
        return Math.toIntExact(
            imgMapper.selectCount(
                LambdaQueryWrapper<Images>().eq(
                    SFunction<Images, Any> { obj: Images -> obj.getUserId() },
                    userid
                )
            )
        )
        //return imgMapper.countimg(userid);
    }

    override fun setImg(images: Images?): Int {
        return imgMapper.setImg(images)
    }

    override fun deleimgname(imgname: String?): Int {
        return imgMapper.deleimgname(imgname)
    }

    override fun deleall(id: String?): Int {
        return imgMapper.deleall(id)
    }

    override fun gettimeimg(time: String?): List<Images?> {
        return imgMapper.getTimeImg(time)
    }

    override fun getUserMemory(userid: String?): Long {
        return imgMapper.getUserMemory(userid)
    }

    override fun getSourceMemory(source: String?): Long {
        return imgMapper.getSourceMemory(source)
    }

    override fun md5Count(images: Images?): Int {
        return imgMapper.md5Count(images)
    }

    override fun selectImgUrlByMD5(md5key: String?): Images {
        return imgMapper.selectImgUrlByMD5(md5key)
    }

    override fun recentlyUploaded(userid: String?): List<Images?> {
        return imgMapper.recentlyUploaded(userid)
    }

    override fun recentlyUser(): List<RecentUserVo?> {
        return imgMapper.recentlyUser()
    }

    override fun getyyyy(userid: String?): List<String?> {
        return imgMapper.getyyyy(userid)
    }

    override fun countByM(images: HomeImgDto?): List<ImageVo?> {
        return imgMapper.countByM(images)
    }

    override fun selectImgUrlByImgUID(imguid: String?): Images {
        return imgMapper.selectOne(
            LambdaQueryWrapper<Images>().eq(
                SFunction<Images, Any> { obj: Images -> obj.getImgUid() },
                imguid
            )
        )
    }
}