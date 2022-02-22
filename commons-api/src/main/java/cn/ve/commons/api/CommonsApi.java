package cn.ve.commons.api;

import cn.ve.feign.pojo.CommonResult;
import cn.ve.commons.pojo.FileParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "commons-provider")
public interface CommonsApi {

    String PUBLIC_ACCESS_FILE = "public_access_file";

    @PostMapping(value = "/feign/commons/1.1.3/getPrivate")
    CommonResult<FileParam> getPrivate(@RequestParam("uri") String uri);

    /**
     * 上传私有文件并打上公共标签
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/feign/commons/1.1.3/uploadPrivateAndTagPublic")
    CommonResult<String> uploadPrivateAndTagPublic(@RequestBody FileParam param);

    @PostMapping(value = "/feign/commons/1.1.3/putPrivateAndTagPublic")
    CommonResult<String> putPrivateAndTagPublic(@RequestBody FileParam param);

    /**
     * 上传私有文件
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/feign/commons/1.1.3/uploadPrivate")
    CommonResult<String> uploadPrivate(@RequestBody FileParam param);

    /**
     * 上传公共文件
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/feign/commons/1.1.3/uploadPublic")
    CommonResult<String> uploadPublic(@RequestBody FileParam param);

    /**
     * 通过文件uri获取私有文件,uri为文件目录+文件名+后缀,以临时url的形式下载文件,短时间有效
     *
     * @param uri
     * @return
     */
    @GetMapping("/admin/v1.1.3/getPreSignFile")
    CommonResult<String> getPreSignFile(@RequestParam(value = "uri", required = false) String uri);
}
