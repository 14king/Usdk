package com.zwwx.plugins.weixin;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zwwx.adapter.PlatformCallBack;
import com.zwwx.adapter.PlatformCallbackWrapper;
import com.zwwx.adapter.ProxyBase;
import com.zwwx.sdk.SDKFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
    private IWXAPI api;
    private PlatformCallbackWrapper cbw;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        WeiXinFactory wxf = (WeiXinFactory)SDKFactory.getPlugin("com.zwwx.plugins.weixin.WeiXinFactory");
        ProxyBase pfp = SDKFactory.getProxy();
        cbw = pfp.platformCallBackWrapper;
        
    	api = WXAPIFactory.createWXAPI(this, wxf.getWeixinAppID(), false);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@Override
	public void onResp(BaseResp resp) {	
		if(resp.errCode == BaseResp.ErrCode.ERR_OK)
			cbw.WeiXinShareCallback(PlatformCallBack.ErrorCode.WeiXinShareSucess
						.ordinal(), "����ɹ�"+resp.errStr);
		else
			cbw.WeiXinShareCallback(PlatformCallBack.ErrorCode.WeiXinShareFail
					.ordinal(), "����ʧ��"+resp.errStr);
		finish();
	}
}