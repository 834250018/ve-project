package cn.ve.message.controller;

import cn.ve.base.util.BeanUtils;
import cn.ve.message.dal.mapper.MessageMessageMapper;
import cn.ve.message.dto.MessageMessageDTO;
import cn.ve.message.param.MessageMessageQO;
import cn.ve.rest.pojo.BaseController;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 系统消息
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/messageMessage")
public class MessageMessageController extends BaseController {

    @Resource
    private MessageMessageMapper messageMessageMapper;

    /**
     * 查询分页
     *
     * @param qo
     * @return
     */
    @GetMapping("/v1.0/page")
    public PageInfo<MessageMessageDTO> queryPage(@ModelAttribute @Valid MessageMessageQO qo) {
        qo.setUserId(getUserId());
        return PageInfo.of(BeanUtils
            .copyList(messageMessageMapper.selectList(new LambdaQueryWrapper<>()), MessageMessageDTO.class));
    }

    /**
     * 查询分页,并根据当前类型消除红点
     *
     * @param qo
     * @return
     */
    @GetMapping("/v1.1.2/pageAndRead")
    public PageInfo<MessageMessageDTO> pageAndRead(@ModelAttribute @Valid MessageMessageQO qo) {
        qo.setUserId(getUserId());
        PageInfo<MessageMessageDTO> of = PageInfo.of(BeanUtils
            .copyList(messageMessageMapper.selectList(new LambdaQueryWrapper<>()), MessageMessageDTO.class));

        // todo 已读此模板类型所有消息
        //        messageSystemAlertExtMapper.readByTemplateType(qo);

        return of;
    }

    /**
     * 我已知晓消息(对某个消息进行已读)
     *
     * @param messageId
     */
    @PostMapping("/v1.0/confirm")
    public void confirm(@RequestParam Long messageId) {
        // todo
        //        messageSystemAlertExtMapper.readById(messageId);
    }

    /**
     * 我已知晓消息(对某个模板下的消息进行已读)
     *
     * @param qo
     */
    @PostMapping("/v1.0/confirmByQO")
    public void confirmByQO(@ModelAttribute @Valid MessageMessageQO qo) {
        qo.setUserId(getUserId());
        // todo
        //        messageSystemAlertExtMapper.readByQO(qo);
    }

}

