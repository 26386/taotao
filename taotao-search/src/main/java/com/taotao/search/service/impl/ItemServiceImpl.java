package com.taotao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.pojo.TbItem;
import com.taotao.search.mapper.ItemMapper;
import com.taotao.search.pojo.Item;
import com.taotao.search.service.ItemService;

/**
 * 向solr的索引库中导入数据
 * 
 * @author wlnner
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;

	public TaotaoResult importAllItems() {
		try {
			List<Item> items = itemMapper.getItemList();
			for (Item item : items) {
				SolrInputDocument doc = new SolrInputDocument();
				doc.addField("id", item.getId());
				doc.addField("item_title", item.getTitle());
				doc.addField("item_sell_point", item.getSell_point());
				doc.addField("item_price", item.getPrice());
				doc.addField("item_image", item.getImage());
				doc.addField("item_category_name", item.getCategory_name());
				doc.addField("item_desc", item.getItem_desc());
				solrServer.add(doc);
			}
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return TaotaoResult.ok();
	}

	public TaotaoResult importItem(TbItem item,String categoryName,String desc) {
		try {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", item.getId());
			doc.addField("item_title", item.getTitle());
			doc.addField("item_sell_point", item.getSellPoint());
			doc.addField("item_price", item.getPrice());
			doc.addField("item_image", item.getImage());
			doc.addField("item_category_name", categoryName);
			doc.addField("item_desc", desc);
			solrServer.add(doc);
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
		return TaotaoResult.ok();
	}
}
