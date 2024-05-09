const webpack = require('webpack');
const path = require('path')
function resolve(dir){
  return path.join(__dirname,dir);
}

module.exports = {
  lintOnSave:false,
  chainWebpack(config){
    config.module.rule('svg').exclude.add(resolve('src/icons')).end()
    config.module.rule('icons').test(/\.svg$/).include.add(resolve('src/icons')).end()
        .use('svg-sprite-loader').loader('svg-sprite-loader').options({symbolId: 'icon-[name]'}).end()
    config.plugin('ignore').use(new webpack.ContextReplacementPlugin(/moment[/\\]locale$/,/zh-cn$/))
    config.module.rule('icons').test(/\.svg$/).include.add(resolve('src/icons')).end()
        .use('svg-sprite-loader').loader('svg-sprite-loader').options({symbolId: 'icon-[name]'}).end()
 },
  devServer:{
    host:'0.0.0.0',port:9536,open:true
  }
}

