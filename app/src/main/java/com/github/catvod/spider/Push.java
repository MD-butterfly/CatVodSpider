package com.github.catvod.spider;

import android.content.Context;

import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.utils.Misc;

import java.util.List;

public class Push extends Spider {

    private Ali ali;

    @Override
    public void init(Context context, String extend) {
        ali = new Ali(extend);
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        String url = ids.get(0).trim();
        if (url.contains("aliyundrive")) return ali.detailContent(ids);
        if (Misc.isVip(url)) return Result.string(vod(url, "官源"));
        else if (Misc.isVideoFormat(url)) return Result.string(vod(url, "直連"));
        else if (url.startsWith("magnet")) return Result.string(vod(url, "磁力"));
        else if (url.startsWith("http")) return Result.string(vod(url, "網頁"));
        else return "";
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) {
        if (flag.contains("AliYun")) return ali.playerContent(flag, id);
        if (flag.equals("官源")) return Result.get().parse().jx().url(id).string();
        if (flag.equals("直連")) return Result.get().url(id).string();
        return Result.get().parse().url(id).string();
    }

    private Vod vod(String url, String type) {
        Vod vod = new Vod();
        vod.setTypeName(type);
        vod.setVodId(url);
        vod.setVodName(url);
        vod.setVodPlayFrom(type);
        vod.setVodPlayUrl("播放$" + url);
        vod.setVodPic("https://pic.rmb.bdstatic.com/bjh/1d0b02d0f57f0a42201f92caba5107ed.jpeg");
        return vod;
    }
}