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
package io.seata.samples.sca.customer.controller;

import io.seata.samples.sca.common.domain.TbDemo;
import io.seata.samples.sca.common.domain.TbUser;
import io.seata.samples.sca.customer.service.TbDemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Description:数据库新建表
 *
 * CREATE TABLE `tb_user` (
 * `id` int(11) NOT NULL AUTO_INCREMENT,
 * `name` varchar(25) NOT NULL,
 * `age` int(3) DEFAULT NULL,
 * PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
 *
 * author: yu.hb
 * Date: 2019-11-01
 */
@RestController
@Slf4j
public class DemoController {

    @Autowired
    TbDemoService demoService;

    /**
     * seata 全局事务控制 **重点研究对象**
     *
     * @param demo
     */
    @PostMapping("/seata/user/add")
    public void add(@RequestBody TbDemo demo) {
        demoService.save(demo);
    }

    @PostMapping("/seata/user/deleteByPrimaryKey/{id}")
    public void deleteByPrimaryKey(@PathVariable(name = "id") Integer id) {
        demoService.deleteByPrimaryKey(id);
    }

    @GetMapping("/seata/user/selectByPrimaryKey/{id}")
    public TbUser selectByPrimaryKey(@PathVariable(name = "id") Integer id) {
        return demoService.selectByPrimaryKey(id);
    }
}
