package novel.storage;

import novel.storage.impl.KanShuZhongNovelStorageIMPL;

public class Bootstrap {

	public static void main(String[] args) throws Exception {
		Processor processor=new KanShuZhongNovelStorageIMPL();
		processor.processor();
		
	}

}
