package io.seata.samples.sca.customer.service;

import io.seata.core.context.RootContext;
import io.seata.samples.sca.common.domain.TbDemo;
import io.seata.samples.sca.common.domain.TbUser;
import io.seata.samples.sca.common.dubbo.api.UserService;
import io.seata.samples.sca.customer.mapper.TbDemoMapper;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
@Transactional
@Slf4j
public class TbDemoService {

    @DubboReference(check = false)
    private UserService userService;

    @Autowired
    private TbDemoMapper demoMapper;

    @GlobalTransactional
    public void save(TbDemo tbDemo) {
        log.info("开始全局事务, Xid:{}", RootContext.getXID());
        // 本地设置
        tbDemo.setName("customer");
        demoMapper.insertSelective(tbDemo); //本地调用 mybatis 插入tb_demo表

        // 远程调用服务
        TbUser user = new TbUser();
        user.setId(tbDemo.getId());
        user.setAge(tbDemo.getAge());
        user.setName(tbDemo.getName());
        userService.add(user);
    }


    @GlobalTransactional
    public TbUser selectByPrimaryKey(Integer id) {
        log.info("开始全局事务, Xid:{}", RootContext.getXID());
        //本服务业务,因为是事务保证的插入,所以这里只需要响应本地的执行结果
        return demoMapper.selectByPrimaryKey(id);
    }

    @GlobalTransactional
    public void deleteByPrimaryKey(Integer id){
        log.info("开始全局事务, Xid:{}", RootContext.getXID());
        //本服务业务

        /*
         * 先查询是否存在用户,再执行删除操作
         * */
        TbUser tbUser = demoMapper.selectByPrimaryKey(id);
        if (tbUser == null) {
            log.info("查无此人, Xid:{}", RootContext.getXID());
            throw new RuntimeException("查无此人"); //本地数据库查无此人
        }
        if(demoMapper.deleteByPrimaryKey(id)!=1) throw new RuntimeException(""); //删除操作涉及多条记录
        log.info("查有此人,开始删除");
        demoMapper.deleteByPrimaryKey(id);


        // 远程调用服务
        userService.deleteByPrimaryKey(id);

    }

}
