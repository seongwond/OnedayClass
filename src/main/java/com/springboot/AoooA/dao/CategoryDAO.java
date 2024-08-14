package com.springboot.AoooA.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DataAccessException;

import com.springboot.AoooA.dto.CategoryDTO;
import com.springboot.AoooA.dto.ImgDTO;

@Mapper
public interface CategoryDAO {

	@Select("select * from category")
	public List<CategoryDTO> selectAllCategory() throws DataAccessException;

	@Select("select class_id from img where img_type = '0'")
	public List<Integer> selectClassId() throws DataAccessException;

	@Select("select * from img where class_id = #{class_id} AND img_type = '0'")
	public List<ImgDTO> getAllList(Integer classId) throws DataAccessException;

	@Select("SELECT class_title FROM class WHERE class_id = #{class_id}")
	ImgDTO selectClassData(Integer classId);

	@Select("SELECT i.img_id, i.class_id, i.img_path, i.img_name, i.img_type, c.class_title, c.class_cost, c.class_regdate "
			+ "FROM img i LEFT JOIN class c ON i.class_id = c.class_id WHERE i.img_type = '0'")
	List<ImgDTO> getAllLists() throws DataAccessException;

	@Select("SELECT i.img_id, i.class_id, i.img_path, i.img_name, i.img_type, c.class_title, c.class_cost, c.class_regdate, c.class_type, c.class_time, LEFT(c.bis_address1, LOCATE(' ', c.bis_address1, LOCATE(' ', c.bis_address1) + 1) - 1) AS bis_address1 "
	        + "FROM img i LEFT JOIN class c ON i.class_id = c.class_id WHERE i.img_type = '0' AND c.class_title LIKE CONCAT('%', #{keyword}, '%') ORDER BY "
	        + "CASE WHEN #{sortType} = 'recent' THEN c.class_regdate END DESC, "
	        + "CASE WHEN #{sortType} = 'high_price' THEN c.class_cost END DESC, "
	        + "CASE WHEN #{sortType} = 'low_price' THEN c.class_cost END ASC LIMIT #{offset}, #{size}")
	List<ImgDTO> selectCategoryByTitleAndSort(@Param("keyword") String keyword, @Param("sortType") String sortType,
	        @Param("offset") int offset, @Param("size") int size) throws DataAccessException;
	
	@Select("SELECT i.img_id, i.class_id, i.img_path, i.img_name, i.img_type, c.class_title, c.class_cost, c.class_regdate, c.class_type, c.class_time, LEFT(c.bis_address1, LOCATE(' ', c.bis_address1, LOCATE(' ', c.bis_address1) + 1) - 1) AS bis_address1 "
			+ "FROM img i LEFT JOIN class c ON i.class_id = c.class_id WHERE i.img_type = '0' AND c.bis_address1 LIKE CONCAT('%', #{area}, '%') AND c.class_title LIKE CONCAT('%', #{keyword}, '%') ORDER BY "
			+ "CASE WHEN #{sortType} = 'recent' THEN c.class_regdate END DESC, "
			+ "CASE WHEN #{sortType} = 'high_price' THEN c.class_cost END DESC, "
			+ "CASE WHEN #{sortType} = 'low_price' THEN c.class_cost END ASC LIMIT #{offset}, #{size}")
	List<ImgDTO> selectCategoryByAreaAndKeyword(@Param("area") String area, @Param("keyword") String keyword,
			@Param("sortType") String sortType, @Param("offset") int offset, @Param("size") int size)
			throws DataAccessException;

	@Select("SELECT i.img_id, i.class_id, i.img_path, i.img_name, i.img_type, c.class_title, c.class_cost, c.class_regdate, c.class_type, c.class_time, LEFT(c.bis_address1, LOCATE(' ', c.bis_address1, LOCATE(' ', c.bis_address1) + 1) - 1) AS bis_address1 "
			+ "FROM img i LEFT JOIN class c ON i.class_id = c.class_id WHERE i.img_type = '0' AND c.class_type = #{class_type} AND c.class_title LIKE CONCAT('%', #{keyword}, '%') ORDER BY "
			+ "CASE WHEN #{sortType} = 'recent' THEN c.class_regdate END DESC, "
			+ "CASE WHEN #{sortType} = 'high_price' THEN c.class_cost END DESC, "
			+ "CASE WHEN #{sortType} = 'low_price' THEN c.class_cost END ASC LIMIT #{offset}, #{size}")
	List<ImgDTO> selectCategoryByTypeAndKeyword(@Param("class_type") Integer class_type,
			@Param("keyword") String keyword, @Param("sortType") String sortType, @Param("offset") int offset,
			@Param("size") int size) throws DataAccessException;

	@Select("SELECT COUNT(*) FROM class WHERE class_title LIKE CONCAT('%', #{keyword}, '%')")
	int getTotalClassCountByKeyword(@Param("keyword") String keyword) throws DataAccessException;

	@Select("SELECT COUNT(*) FROM class WHERE bis_address1 LIKE CONCAT('%', #{area}, '%') AND class_title LIKE CONCAT('%', #{keyword}, '%')")
	int getTotalClassCountByAreaAndKeyword(@Param("area") String area, @Param("keyword") String keyword)
			throws DataAccessException;

	@Select("SELECT COUNT(*) FROM class WHERE class_type = #{class_type} AND class_title LIKE CONCAT('%', #{keyword}, '%')")
	int getTotalClassCountByTypeAndKeyword(@Param("class_type") Integer class_type, @Param("keyword") String keyword)
			throws DataAccessException;
}