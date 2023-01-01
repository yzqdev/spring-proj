package cn.hellohao.quartz.job;

import cn.hellohao.model.entity.Images;
import cn.hellohao.model.entity.StorageKey;
import cn.hellohao.service.ImgService;
import cn.hellohao.service.ImgTempService;
import cn.hellohao.service.KeysService;
import cn.hellohao.service.impl.*;
import cn.hellohao.util.LocUpdateImg;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@EnableScheduling
public class FirstJob {

	@Resource
	private ImgService imgService;
	@Resource
	private NOSImageupload nosImageupload;
	@Resource
	private OSSImageupload ossImageupload;
	@Resource
	private COSImageupload cosImageupload;
	@Resource
	private KODOImageupload kodoImageupload;
	@Resource
	private USSImageupload ussImageupload;
	@Resource
	private UFileImageupload uFileImageupload;
	@Resource
	private FTPImageupload ftpImageupload;
	@Resource
	ImgTempService imgTempService;
	@Resource
	KeysService keysService;

	private static FirstJob firstJob;

	@PostConstruct
	public void init() {
		firstJob = this;
	}

	//调用的任务主体
	public void task() {
//		System.out.println("定时任务.//开始");
		try{
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String currdate = format.format(LocalDateTime.now());
			List<Images> imagesList = imgTempService.selectDelImgUidList(currdate);
			if(imagesList.size()==0){
//				System.out.println("定时任务.//没有期限图片");
				return;
			}
			for (Images images : imagesList) {
				imgTempService.delImgAndExp(images.getImgUid());
				imgService.deleimgForImgUid(images.getImgUid());
				StorageKey storageKey = keysService.selectKeys(images.getSource());
				if(storageKey.getStorageType()==1){
					firstJob.nosImageupload.delNOS(storageKey.getId(),images.getImgName());
				}else if (storageKey.getStorageType()==2){
					firstJob.ossImageupload.delOSS(storageKey.getId(), images.getImgName());
				}else if(storageKey.getStorageType()==3){
					firstJob.ussImageupload.delUSS(storageKey.getId(), images.getImgName());
				}else if(storageKey.getStorageType()==4){
					firstJob.kodoImageupload.delKODO(storageKey.getId(), images.getImgName());
				}else if(storageKey.getStorageType()==5){
					LocUpdateImg.deleteLOCImg(images.getImgName());
				}else if(storageKey.getStorageType()==6){
					firstJob.cosImageupload.delCOS(storageKey.getId(),images.getImgName());
				}else if(storageKey.getStorageType()==7){
					firstJob.ftpImageupload.delFTP(storageKey.getId(), images.getImgName());
				}else if(storageKey.getStorageType()==8){
					firstJob.uFileImageupload.delUFile(storageKey.getId(), images.getImgName());
				}else{
					System.err.println("未获取到对象存储参数，上传失败。");
				}
			}
		}catch (Exception e){
			System.err.println("任务查询期限图像发生错误");
			e.printStackTrace();
		}


	}



}
