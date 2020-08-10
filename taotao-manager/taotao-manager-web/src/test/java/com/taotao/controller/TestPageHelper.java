package com.taotao.controller;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;

public class TestPageHelper {

	@Test
	public void testPageHelper() throws Exception {
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
		TbItemExample example=new TbItemExample();
		PageHelper.startPage(1, 10);
		List<TbItem> list = tbItemMapper.selectByExample(example);
		for (TbItem tbItem : list) {
			System.out.println(tbItem.getTitle());
		}
		PageInfo<TbItem> pageInfo=new PageInfo<>(list);
		System.out.println("共有商品:"+pageInfo.getTotal());
	}
}
