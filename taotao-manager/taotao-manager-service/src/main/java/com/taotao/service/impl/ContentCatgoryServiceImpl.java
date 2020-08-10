package com.taotao.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
import com.taotao.service.ContentCatgoryService;

/**
 * 内容分类管理
 * @author wlnner
 *
 */
@Service
public class ContentCatgoryServiceImpl implements ContentCatgoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	public List<EUTreeNode> getCategoryList(Long parentId){
		List<EUTreeNode> list=new ArrayList<>();
		TbContentCategoryExample example=new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> contentCategories = contentCategoryMapper.selectByExample(example);
		for (TbContentCategory tbContentCategory : contentCategories) {
			EUTreeNode euTreeNode=new EUTreeNode();
			euTreeNode.setId(tbContentCategory.getId());
			euTreeNode.setText(tbContentCategory.getName());
			euTreeNode.setState(tbContentCategory.getIsParent()?"closed":"open");
			list.add(euTreeNode);
		}
		return list;
	}
	
	public TaotaoResult insertContentCategory(Long parentId,String name){
		TbContentCategory contentCategory=new TbContentCategory();
		contentCategory.setName(name);
		contentCategory.setParentId(parentId);
		contentCategory.setIsParent(false);
		contentCategory.setSortOrder(1);
		contentCategory.setStatus(1);//状态:1(正常)2(删除)
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//添加数据
		contentCategoryMapper.insert(contentCategory);
		//插入新的节点之后,原来的叶子结点要改成父节点
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		return TaotaoResult.ok(contentCategory);
	}
	
	public TaotaoResult deleteContentCategory(Long id){
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		Long parentId=contentCategory.getParentId();
		//判断此节点是否有子节点
		if(hasChildCat(id)){
			//有子节点,递归删除
			List<Long> ids=new ArrayList<>();
			ids.add(id);
			getAllChildCat(id,ids);
			//System.out.println(ids);
			//TODO:批量删除内容分类
			contentCategoryMapper.batchDeleteById(ids);
		}else{
			//TODO:单个删除id这行数据
			contentCategoryMapper.deleteByPrimaryKey(id);
		}
		//删除此节点之后判断其父节点是否还有其他的子节点
		if(!hasChildCat(parentId)){
			//此时父节点无子节点
			TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
			parent.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		return TaotaoResult.ok();
	}

	//更新商品分类名
	public TaotaoResult updateContentCategoryName(Long id,String name){
		TbContentCategory contentCategory=new TbContentCategory();
		contentCategory.setId(id);
		contentCategory.setName(name);
		contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
		return TaotaoResult.ok();
	}
	
	//根据parentId递归出每一个节点
	private List<Long> getAllChildCat(Long parentId,List<Long> ids){
		TbContentCategoryExample example=new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		for (TbContentCategory tbContentCategory : list) {
			if(tbContentCategory.getIsParent()){
				getAllChildCat(tbContentCategory.getId(),ids);
			}
			ids.add(tbContentCategory.getId());
		}
		return ids;
	}
	
	//判断一个节点是否有子节点
	private boolean hasChildCat(Long parentId){
		List<TbContentCategory> list=this.getContentCategoryByParentId(parentId);
		return list!=null && list.size()>0;
	}
	
	//根据parentId获取内容分类
	private List<TbContentCategory> getContentCategoryByParentId(Long parentId){
		TbContentCategoryExample example=new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		return list;
	}
	
}
