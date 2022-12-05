/*
 *  Copyright 1999-2021 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.samples.sca.provider.dubbo;

import io.seata.samples.sca.common.domain.TbUser;
import io.seata.samples.sca.common.dubbo.api.UserService;
import io.seata.samples.sca.provider.mapper.TbUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * author: yu.hb
 * Date: 2019-11-01
 */
@DubboService
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper userMapper;

    @Transactional //只是本地事务 它只能回滚本地自己的数据库连接 无法回滚其他微服务连接数据库
    @Override
    public void add(TbUser user) {
        log.info("add user:{}", user);
        //本地设置名称
        user.setName("provider");
        userMapper.insert(user);
        //  测试 seata 全局事务(主动抛出异常应该两条插入记录都会失效)
//        throw new RuntimeException("");
    }

    @Transactional
    @Override
    public void deleteByPrimaryKey(Integer  id) {
        log.info("delete user id:{}", id);

        userMapper.deleteByPrimaryKey(id);
        //  测试 seata 全局事务(主动抛出异常应该两条插入记录都会失效)
//        throw new RuntimeException("");
    }
}
