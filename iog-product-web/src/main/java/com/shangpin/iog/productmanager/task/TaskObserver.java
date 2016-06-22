
package com.shangpin.iog.productmanager.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.shangpin.iog.dto.CsvSupplierInfoDTO;
@Setter
@Getter
public class TaskObserver implements Observer {
	
	private TaskObserver(){};
	private static TaskObserver taskObserver;
	public static TaskObserver getTaskObserver(){
		if (null==taskObserver) {
			taskObserver = new TaskObserver();
		}
		return taskObserver;
	}
	private TaskController taskController;
	private Observable monitor;
	
	@Override
	public void update(Observable monitor, Object changedMap) {
		HashMap<String, CsvSupplierInfoDTO> changeMap = (HashMap<String, CsvSupplierInfoDTO>) changedMap;
		System.out.println("++++发生变化+++++"+changeMap.toString());
		
		taskController.resetTask(changeMap);
		
	}

}
