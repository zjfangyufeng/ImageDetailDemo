# ImageDetailDemo
viewpager图片查看 缩放 拖拽（高仿微信图片浏览效果）

怎么用：

ArrayList<String> strings = new ArrayList<>();
strings.add(url1);
strings.add(url2);
strings.add(url3);
startActivity(ImageDetailActivity.getMyStartIntent(this,strings,0, ImageDetailActivity.local_file_path));

参数一：context 
参数二：图片地址的集合
参数三：默认选中的postion
参数四：图片地址的类型，目前支持网络url类型与本地文件地址类型。

需要注意：

if(pathType == url_path){
//							tempBitmap = AsyncListIconLoader.getInstance().loadImageFromInternet(
//									AsyncListIconLoader.getPicPrefix(screenshot_samples.get(vh.pos)),
//									AsyncListIconLoader.FULLWIDTH,AsyncListIconLoader.FULLWIDTH, false);// 获取网络图片
}else if(pathType == local_file_path){
	tempBitmap = BitmapFactory.decodeResource(getResources(),Integer.parseInt(screenshot_samples.get(vh.pos)));
}

本代码为demo版，为了方便演示，并没有加入网络图片下载模块，所以图片取的是项目中自带的图片。实际使用中请自行在上述代码中加入网络模块下载图片---url地址模式（如果是本地图片文件模式，解码本地图片文件即可）
