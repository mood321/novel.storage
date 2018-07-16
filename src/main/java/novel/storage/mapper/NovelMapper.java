package novel.storage.mapper;

import java.util.List;

import novel.spider.entity.Novel;



public interface NovelMapper {
	public int deleteByPrimaryKey(Long id);

	int insert(Novel record);

    public int insertSelective(Novel record);

    public Novel selectByPrimaryKey(Long id);

    public int updateByPrimaryKeySelective(Novel record);

    public int updateByPrimaryKey(Novel record);
    public int batchInsert(List<Novel> novels);
}