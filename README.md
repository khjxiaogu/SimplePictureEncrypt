# 简单图片加密  
一个简单的图片加密工具，基于柏林噪音和随机密码。  
使用了CBC方法，大幅度的降低了输出的可读性。  
这只是个理论项目，并没有经过很多测试，可能被蛮力破解，所以安全性不能保证，仅供娱乐，请勿用于任何生产环境！  
如果需要创建个性化的加密，只需要随机打乱柏林噪音类的排列数即可。  
请发送原图，否则可能导致解密失败！  
# Simple Picture Encrypt  
A Simple Picture Encrypt based on Random by random password, perlin noise key generation and xor encryption with vectors.  
It uses some sort of CBC encryption, which mainly used to remove traits from picture, Reducing readability.  
This is just a POC project. It has not been tested, and may be brute force crack. So security is not guaranteed, you may not use in any sort of production scenes.  
To create your customized encryption, just simply shuffle the permutation of perlin noise function may of great effect.   
Image decryption may not be effective if the image is compressed during transmission!  
