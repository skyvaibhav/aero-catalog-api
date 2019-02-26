package com.hc.bo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hc.db.bean.GridKey;
import com.hc.db.bean.GridParam;
import com.hc.db.dao.ICategoryDao;
import com.hc.db.dao.ILanguageSpecificDao;
import com.hc.db.dao.IQuestionSetDao;
import com.hc.db.entity.Category;
import com.hc.db.entity.LanguageSpecific;
import com.hc.db.entity.QuestionSet;
import com.hc.db.entity.mapper.CategoryMapper;
import com.hc.db.entity.mapper.LanguageSpecificMapper;
import com.hc.db.entity.mapper.QuestionSetMapper;
import com.hc.jsonbean.JsonCategoryKeyMap;
import com.hc.util.constant.AppConstant;

/**
 * @author fivedev
 * @since 13-05-2016
 */
@Component
public class CategoryBo {

	private final static Logger logger = Logger.getLogger(CategoryBo.class);

	public static final String ADD_SUCCESS = "The Category was added successfully.";
	public static final String UPDATE_SUCCESS = "The Category was updated successfully.";
	public static final String DELETE_SUCCESS = "The Category was deleted successfully";
	public static final String ALREADY_EXISTS = "This Category already exists. Try another one.";
	public static final String ALREADY_IN_USE = "This Category is already in use.";

	@Autowired
	private ICategoryDao categoryDao;

	@Autowired
	private ILanguageSpecificDao languageSpecificDao;

	@Autowired
	private IQuestionSetDao questionSetDao;


	public Map<String, Object> searchByGridParam(GridParam grid_param, long language_id){
		Map<String, Object> gridResponse = new HashMap<>();
		List<JsonCategoryKeyMap> jsonCategories = new ArrayList<>();
		List<Category> categories = new ArrayList<>();

		categories = categoryDao.findAll("WHERE deleted_on IS NULL AND deleted_by = 0", "ORDER BY modified_on DESC", new CategoryMapper());
		for (Category item : categories) {
			JsonCategoryKeyMap category = new JsonCategoryKeyMap();

			LanguageSpecific ls = languageSpecificDao.findById(item.getCategory_id(), language_id, AppConstant.ENTITY_CATEGORY, new LanguageSpecificMapper());

			category.set_id(item.getCategory_id());
			category.set_name(ls.getEntity_text());

			jsonCategories.add(category);
		}

		Predicate<JsonCategoryKeyMap> criteria = null;
		Comparator<JsonCategoryKeyMap> orderBy = null;

		if(grid_param.getFilters().size() > 0) {
			for(GridKey gridKey : grid_param.getFilters()) {
				if(gridKey.getProperty().equals("categoryName")) {
					criteria = x -> x.get_name().toLowerCase().contains(gridKey.getValue().toString().toLowerCase());
				}
			}
		}

		if(grid_param.getSort().size() > 0) {
			for(GridKey gridKey : grid_param.getSort()) {
				if(gridKey.getProperty().equals("categoryName")) {
					if(gridKey.getValue().equals("desc")) {
						orderBy = (x, y) -> y.get_name().toLowerCase().compareTo(x.get_name().toLowerCase());
					} else {
						orderBy = (x, y) -> x.get_name().toLowerCase().compareTo(y.get_name().toLowerCase());
					}
				}
			}
		}

		if(criteria != null) {
			jsonCategories = jsonCategories.stream().filter(criteria).collect(Collectors.toList()); 
		}

		gridResponse.put("totalRecords", jsonCategories.size());

		if(orderBy != null) {
			jsonCategories = jsonCategories.stream().sorted(orderBy).collect(Collectors.toList());
		}

		jsonCategories = jsonCategories.stream()
				.skip(grid_param.getStart() * grid_param.getLimit())
				.limit(grid_param.getLimit())
				.collect(Collectors.toList());

		gridResponse.put("resultset", jsonCategories);

		return gridResponse;
	}

	/**
	 * @param language_id
	 * @return
	 */
	public List<JsonCategoryKeyMap> getAllCategories(long language_id){
		List<JsonCategoryKeyMap> categoryBeans = new ArrayList<>();
		List<Category> categories = new ArrayList<>(); 

		categories = categoryDao.findAll("WHERE deleted_by = 0 AND deleted_on IS NULL", new CategoryMapper());
		
		for(Category item : categories) {			
			LanguageSpecific ls = languageSpecificDao.findById(item.getCategory_id(), language_id, AppConstant.ENTITY_CATEGORY, new LanguageSpecificMapper());
			if(ls != null){
				categoryBeans.add(new JsonCategoryKeyMap(item.getCategory_id(), ls.getEntity_text()));
			} else {
				return null;
			}
		}
		
		return categoryBeans.stream()
				.sorted((x, y) -> x.get_name().compareTo(y.get_name()))
				.collect(Collectors.toList());
	}
	
	/**
	 * @param category_id
	 * @param language_id
	 * @return
	 */
	public JsonCategoryKeyMap getCategoryDetails(long category_id, long language_id) {
		JsonCategoryKeyMap item = null;
		Category category = categoryDao.findById(category_id, new CategoryMapper());

		if (category != null) {
			LanguageSpecific ls = languageSpecificDao.findById(category_id, language_id, AppConstant.ENTITY_CATEGORY, new LanguageSpecificMapper());
			item = new JsonCategoryKeyMap(category_id, ls.getEntity_text());
		}

		return item;
	}

	/**
	 * @param json_category
	 * @param language_id
	 * @param tran_user_id
	 * @return
	 */
	public Map<String, Object> addUpdate(JsonCategoryKeyMap json_category, long language_id, long tran_user_id) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", AppConstant.FAILURE);

		if(json_category == null || json_category.get_name().length() == 0) {
			return response;
		}

		try {
			String criteria = "WHERE ls.entity_text = '" + json_category.get_name() + "'";

			if (json_category.get_id() != null) {
				criteria = criteria.concat(" AND c.category_id <> " + json_category.get_id());
			}

			if(categoryDao.count(criteria, language_id) == 0) {
				Category c;
				String transactionMode = "";

				if(json_category.get_id() == null) {
					c = new Category();
					transactionMode = "INSERT";
				} else {
					c = categoryDao.findById(json_category.get_id(), new CategoryMapper());
					transactionMode = "UPDATE";
				}

				if(c.getCategory_id() == null) {
					c.setCreated_by(tran_user_id);
					c.setCreated_on(new Date());
				}
				c.setModified_by(tran_user_id);
				c.setModified_on(new Date());								

				categoryDao.save(c);

				LanguageSpecific ls;

				if(transactionMode.equals("INSERT")) {
					ls = new LanguageSpecific();
				} else {
					ls = languageSpecificDao.findById(c.getCategory_id(), language_id, AppConstant.ENTITY_CATEGORY, new LanguageSpecificMapper());
				}

				ls.setEntity_desc("");
				ls.setEntity_id(c.getCategory_id());
				ls.setEntity_text(json_category.get_name());
				ls.setEntity_type(AppConstant.ENTITY_CATEGORY);
				ls.setLanguage_id(language_id);

				languageSpecificDao.save(ls);

				if(transactionMode.equals("INSERT")) {
					response.put("id", c.getCategory_id());
					response.put("message", ADD_SUCCESS);
				} else {
					response.put("message", UPDATE_SUCCESS);
				}
			} else {
				response.put("message", ALREADY_EXISTS);
			}
		} catch (Exception e) {
			logger.error("Error:", e);
		}

		return response;
	}

	/**
	 * @param category_ids
	 * @param tran_user_id
	 * @return
	 */
	public Map<String, Object> remove(List<Long> category_ids, long tran_user_id) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", AppConstant.FAILURE);

		if(category_ids.size() <= 0) {
			return response;
		}

		try {
			String idStr = StringUtils.join(category_ids, ",");

			// check Question Set -> Category association
			List<QuestionSet> questionSets = questionSetDao.findAll("WHERE deleted_by = 0 AND deleted_on IS NULl AND category_id IN (" + idStr + ")", new QuestionSetMapper());

			if(questionSets.size() > 0) {
				response.put("message", ALREADY_IN_USE);
			} else {				
				List<Category> categories = categoryDao.findAll("WHERE deleted_on IS NULL AND deleted_by = 0 AND category_id IN (" + idStr + ")", new CategoryMapper());

				for (Category c : categories) {
					c.setDeleted_by(tran_user_id);
					c.setDeleted_on(new Date());
				}
				categoryDao.save(categories);
				response.put("message", DELETE_SUCCESS);
			}
		} catch (Exception e) {
			logger.error("Error:", e);
		}

		return response;
	}
}
