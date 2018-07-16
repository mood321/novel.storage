package novel.storage.impl;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import novel.spider.entity.Novel;
import novel.spider.interfaces.INovelSpider;
import novel.spider.util.NoelSpiderrFactory;
import novel.storage.Processor;

public abstract class AbstractNovelStorage implements Processor {
	protected static final Map<String, String> TASKS = new TreeMap<>();
	//
	protected SqlSessionFactory sqlSessionFactory;

	public AbstractNovelStorage() throws Exception {
		sqlSessionFactory=new SqlSessionFactoryBuilder().build(
				new FileInputStream("conf/SqlMapConfig.xml"));
	}
	
	@Override
	public void processor() {

		ExecutorService service = Executors.newFixedThreadPool(TASKS.size());
		List<Future<String>> futures = new ArrayList<>(TASKS.size());
		for (Entry<String, String> entry : TASKS.entrySet()) {
			final String key = entry.getKey();
			final String value = entry.getValue();
			futures.add(service.submit(new Callable<String> () {
				@Override
				public String call() throws Exception {
					INovelSpider spider = NoelSpiderrFactory.getNovelSpider(value);
					Iterator<List<Novel>> iterator = spider.iterator(value, 10);
					while (iterator.hasNext()) {
						System.err.println("开始抓取[" + key + "] 的 URL:" + spider.next());
						List<Novel> novels = iterator.next();
						for (Novel novel : novels) {
							novel.setFirstLetter(key.charAt(0));	//设置小说的名字的首字母
						}
						SqlSession session = sqlSessionFactory.openSession();
						session.insert("batchInsert", novels);
						session.commit();
						session.close();
						Thread.sleep(1_000);
					}
					return key;
				}
				
			}));
		}
		service.shutdown();
		for (Future<String> future : futures) {
			try {
				System.out.println("抓取" + future.get() + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
