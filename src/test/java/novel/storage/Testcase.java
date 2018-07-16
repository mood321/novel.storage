package novel.storage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import novel.spider.entity.Novel;
import novel.spider.imp.novel.KanSZNovelSpider;
import novel.spider.interfaces.INovelSpider;
import novel.storage.impl.KanShuZhongNovelStorageIMPL;

public class Testcase {
	
	/**
	 * 测试mybatis
	 * @throws FileNotFoundException 
	 */
	@Test
	public void getMyBasti() throws FileNotFoundException {
		SqlSession session = new SqlSessionFactoryBuilder().build(new FileInputStream("conf/SqlMapConfig.xml")).openSession();
		System.out.println(session);
		
		
		Novel novel=new Novel();
		//SqlSession session = sqlSessionFactory.openSession();
		//INovelSpider ns=new KanSZNovelSpider();
		//List<Novel> novels =ns.getNovel("http://www.kanshuzhong.com/map/V/1/", 3);
		
		novel.setAuthor("zs");novel.setName("name");
		novel.setUrl("baidi.com");
		session.insert("insert", novel);
		session.commit();
		session.close();
	}
	@Test
	public void getKanShuZhong() throws Exception {
		Processor processor=new KanShuZhongNovelStorageIMPL();
		processor.processor();
	}
}
