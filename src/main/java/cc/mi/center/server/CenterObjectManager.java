package cc.mi.center.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cc.mi.core.binlog.data.BinlogData;
import cc.mi.core.generate.msg.BinlogDataModify;
import cc.mi.core.generate.stru.BinlogInfo;
import cc.mi.core.server.ServerObjectManager;
import io.netty.channel.Channel;

public class CenterObjectManager extends ServerObjectManager {
	
	public void sendBinlogData(Channel channel, int fd, String binlogId) {
		BinlogData binlogData = this.get(binlogId);
		if (binlogData != null) {
			// 消息对象
			BinlogDataModify bdm = new BinlogDataModify();
			// 消息内容
			List<BinlogInfo> binlogDataList = new ArrayList<>(1);
			BinlogInfo binlogInfo = binlogData.packNewBinlogInfo();
			binlogDataList.add(binlogInfo);
			bdm.setBinlogDataList(binlogDataList);
			// 如果是发给客户端的需要设置
			bdm.setFD(fd);
			
			channel.writeAndFlush(bdm);
		}
	}
	
	public void sendOwnerAllBinlogData(Channel channel, int fd, String binlogOwnerId) {
		// 消息对象
		BinlogDataModify bdm = new BinlogDataModify();
		
		// 消息内容
		List<BinlogData> result = new LinkedList<>();
		this.getDataSetAllObject(binlogOwnerId, result);
		List<BinlogInfo> binlogDataList = new ArrayList<>(result.size());
		for (BinlogData binlogData : result) {
			BinlogInfo binlogInfo = binlogData.packNewBinlogInfo();
			binlogDataList.add(binlogInfo);
		}
		bdm.setBinlogDataList(binlogDataList);
		// 如果是发给客户端的需要设置
		bdm.setFD(fd);
		
		channel.writeAndFlush(bdm);
	}
}
