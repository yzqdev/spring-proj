package im.zhaojun.zfile.controller.admin;

import im.zhaojun.zfile.model.dto.CacheInfoDTO;
import im.zhaojun.zfile.model.support.ResultBean;
import im.zhaojun.zfile.service.DriveConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 缓存 Controller
 *
 * @author zhaojun
 */
@RestController
@RequestMapping("/admin/cache")
public class CacheController {

    @Resource
    private DriveConfigService driveConfigService;

    @PostMapping("/{driveId}/enable")
    public ResultBean enableCache(@PathVariable("driveId") String driveId) {
        driveConfigService.updateCacheStatus(driveId, true);
        return ResultBean.success();
    }


    @PostMapping("/{driveId}/disable")
    public ResultBean disableCache(@PathVariable("driveId") String driveId) {
        driveConfigService.updateCacheStatus(driveId, false);
        return ResultBean.success();
    }


    @GetMapping("/{driveId}/info")
    public ResultBean cacheInfo(@PathVariable("driveId") String driveId) {
        CacheInfoDTO cacheInfo = driveConfigService.findCacheInfo(driveId);
        return ResultBean.success(cacheInfo);
    }


    @PostMapping("/{driveId}/refresh")
    public ResultBean refreshCache(@PathVariable("driveId") String driveId, String key) throws Exception {
        driveConfigService.refreshCache(driveId, key);
        return ResultBean.success();
    }

    @PostMapping("/{driveId}/auto-refresh/start")
    public ResultBean enableAutoRefresh(@PathVariable("driveId") String driveId) {
        driveConfigService.startAutoCacheRefresh(driveId);
        return ResultBean.success();
    }


    @PostMapping("/{driveId}/auto-refresh/stop")
    public ResultBean disableAutoRefresh(@PathVariable("driveId") String driveId) {
        driveConfigService.stopAutoCacheRefresh(driveId);
        return ResultBean.success();
    }

    @PostMapping("/{driveId}/clear")
    public ResultBean clearCache(@PathVariable("driveId") String driveId) {
        driveConfigService.clearCache(driveId);
        return ResultBean.success();
    }

}