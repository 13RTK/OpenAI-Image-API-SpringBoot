# 1. 概述
- 该项目只是基于OpenAI提供的免费接口，使用SpringBoot进行了简单的封装，并不包含任何图像生成算法和模型
- 运行该项目的前提是拥有一个Open AI的账号，并且生成一个自己的API-KEY
- 由于OpenAI的图像接口只能够根据英文描述来生成内容，因此本项目接入了讯飞的机器翻译API，请自备一个机器翻译API，或者替换为自己的翻译API


# 2. 运行
- 该项目基于SpringBoot 3.0.0版本构建，请自备JDK17，或者将SpringBoot的版本自行降为2.X.X
- 只需要将配置文件中的"key"配置项修改为你自己的API-KEY即可（前面的"Bearer"不要删掉）

# 3. 文档
- [OpenAI的文档](https://beta.openai.com/docs/api-reference/images)
- [讯飞机器翻译的文档](https://www.xfyun.cn/doc/nlp/niutrans/API.html#%E6%8E%A5%E5%8F%A3%E8%AF%B4%E6%98%8E)