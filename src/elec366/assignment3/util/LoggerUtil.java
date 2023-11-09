package elec366.assignment3.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerUtil {

	public static Logger createLogger(String tag) {
		String randomName = tag + "-" + ThreadLocalRandom.current().nextInt(1000000); 
		return createLogger(tag, randomName); 
	}
	
	public static Logger createLogger(String prefix, String tag) {
		Logger logger = Logger.getLogger(tag); 
		logger.setUseParentHandlers(false);
		logger.addHandler(new ConsoleHandler() {
			public ConsoleHandler delegate() {
				this.setFormatter(new Formatter() {
					private Date dt = new Date(); 
					private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
					@Override
					public String format(LogRecord record) {
						StringBuilder sb = new StringBuilder(); 
						if(!prefix.isEmpty()) sb.append("[").append(prefix).append("] "); 
						this.dt.setTime(record.getMillis()); 
						sb.append("[").append(this.sdf.format(this.dt)).append("] ["); 
						sb.append(record.getLevel()).append("] ").append(record.getMessage()).append("\n"); 
						return sb.toString(); 
					}
				});
				return this; 
			}
		}.delegate());
		return logger;
	}

}
