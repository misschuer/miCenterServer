package cc.mi.center.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cc.mi.core.binlog.data.BinlogData;
import cc.mi.core.constance.IdentityConst;
import cc.mi.core.generate.msg.BinlogDataModify;
import cc.mi.core.generate.stru.BinlogInfo;
import cc.mi.core.server.ServerObjectManager;
import io.netty.channel.Channel;

public class CenterObjectManager extends ServerObjectManager {
	
	public CenterObjectManager() {
		super(IdentityConst.SERVER_TYPE_CENTER);
	}
	
	public void sendBinlogData(Channel channel, int fd, String binlogId) {
		BinlogData binlogData = this.get(binlogId);
		if (binlogData != null) {
			// 消息对象
			BinlogDataModify bdm = new BinlogDataModify();
			// 消息内容
			List<BinlogInfo> binlogInfoList = new ArrayList<>(1);
			BinlogInfo binlogInfo = binlogData.packNewBinlogInfo();
			binlogInfoList.add(binlogInfo);
			bdm.setBinlogInfoList(binlogInfoList);
			// 如果是发给客户端的需要设置
			bdm.setBaseFd(fd);
			
			channel.writeAndFlush(bdm);
		}
	}
	
	public void sendOwnerAllBinlogData(Channel channel, int fd, String binlogOwnerId) {
		// 消息对象
		BinlogDataModify bdm = new BinlogDataModify();
		
		// 消息内容
		List<BinlogData> result = new LinkedList<>();
		this.getDataSetAllObject(binlogOwnerId, result);
		List<BinlogInfo> binlogInfoList = new ArrayList<>(result.size());
		for (BinlogData binlogData : result) {
			BinlogInfo binlogInfo = binlogData.packNewBinlogInfo();
			binlogInfoList.add(binlogInfo);
		}
		bdm.setBinlogInfoList(binlogInfoList);
		// 如果是发给客户端的需要设置
		bdm.setBaseFd(fd);
		
		channel.writeAndFlush(bdm);
	}

	@Override
	protected BinlogData createBinlogData(String guid) {
		return new BinlogData(1 << 6, 1 << 6);
	}
}
