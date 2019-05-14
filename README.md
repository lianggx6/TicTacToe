
## 五子棋  

### 导言
1. **问题描述：**  
 &emsp; 本次实验要求设计一个五子棋的AI，要求能与人类对战，并具有较好的用户交互界面。  
     
2. **背景介绍：**    
&emsp; 五子棋是世界智力运动会竞技项目之一，是一种两人对弈的纯策略型棋类游戏，是世界智力运动会竞技项目之一，通常双方分别使用黑白两色的棋子，下在棋盘直线与横线的交叉点上，先形成5子连线者获胜。棋盘由横纵各15条等距离，垂直交叉的平行线构成，在棋盘上，横纵线交叉形成了225个交叉点为对弈时的落子点。  
&emsp; 尽管五子棋先后于1992年、2001年被计算机证明原始无禁手、原始有禁手规则下先手必胜，在五子棋专业比赛中采用现代开局规则（如基于无禁手的两次交换规则（Swap-2），基于有禁手的索索夫-8规则（Soosorv-8））远比原始规则复杂，并未被终结。然而，相比电脑象棋，电脑五子棋的发展是缓慢的。顶级五子棋程序虽长于局部计算，但缺乏大局观，因此很多五子棋专家相信目前的五子棋程序依旧无法超越最强的人类棋手。  
    
3. **使用方法：**  
&emsp; 极大极小搜索算法、α-β剪枝算法。  
4. **方法介绍：**  
&emsp; 极大极小策略是考虑双方对弈若干步之后，从可能的步中选一步相对好的步法来走，即在有限的搜索深度范围内进行求解。 定义一个静态估价函数f，以便对棋局的态势做出优劣评估。    
 **规定：**  
&emsp;（1）max和min代表对弈双方；（2）p代表一个棋局（即一个状态）；（3）有利于MAX的态势，f(p)取正值；（4）有利于MIN的态势，f(p)取负值；（5）态势均衡，f(p)取零值。    
**MINMAX的基本思想：**  
&emsp;（1）当轮到MIN走步时，MAX应该考虑最坏的情况（即f(p)取极小值）（2）当轮到MAX走步时，MAX应该考虑最好的情况（即f(p)取极大值）（3）相应于两位棋手的对抗策略，交替使用（1）和（2）两种方法传递倒推值。   
 &emsp; α-β剪枝算法是采用有界深度优先策略进行搜索，当生成节点达到规定的深度时，就立即进行静态估计，而一旦某个非端节点有条件确定倒推值时，就立即赋值。  
**定义：**  
&emsp; α值：有或后继的节点，取当前子节点中的最大倒推值为其下界，称为α值。节点倒推值>=α；  
&emsp; β值：有与后继的节点，取当前子节点中的最小倒推值为其上界，称为β值。节点倒推值<=β；  
**α-β 剪枝：**  
&emsp; （1） β剪枝：节点x的α值不能降低其父节点的β值，x以下的分支可停止搜索，且x的倒推值为α；  
&emsp; （2） α 剪枝：节点x的β值不能升高其父节点的α值，x以下的分支可停止搜索，且x的倒推值为β；  
    
### 实验过程  
1. **对弈AI落子的程序设计**  
&emsp; AI落子的函数如下。当前落子轮到AI时，进行落子。如果是先手第一步，直接下天元位置，否则调用choose函数选择一个落子位置。最后改变下棋的次序状态。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/01.png?raw=true)  
&emsp; 在计算落子位置之前，有两个预备的函数。一个是就算某一条线的分值，一个是计算整个棋盘的分值，后者需调用前者。而计算落子时，又需要计算整个棋盘的分值。  
&emsp; 如下是计算某一条线的分值，这一条线有6个字，在计算整个棋盘的分值时逐一取出。然后根据每种情况返回分值。包括连五子，连四子，连三子，连二子，连一子。以及上边这些被堵了一头情况，如果堵了一侧，则分值降一等。每种情况的分值相差一个数量级。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/02.png?raw=true)
&emsp; 计算棋盘的分值的函数如下。SumGoal为总分数，然后遍历每一个点。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/03.png?raw=true)
&emsp;  每一个点的处理如下。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/04.png?raw=true)  
&emsp; 	对于每一个点，我们朝其四个方向取6个点（含其自身）。如果某一方向超出棋盘范围，就设一个无效值。每取一次，就调用刚刚那个函数计算一次分值，加到sumGoal。四个方向均如此，遍历的每个子均如此，最后得出当前棋局对于color方的局势分数。  
&emsp;	 接下来是使用极大值极小值搜索和α-β剪枝搜索落子位置。首先，深拷贝一个棋盘，用来进行预估。  
 ![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/05.png?raw=true)  
&emsp;	 然后是第一层极大值的搜索，首先将MAX值设一个最小值。然后遍历棋盘，如果某一点无子且其周围有子，则为一个预估点，假设这一点放置了本方的棋子。然后进行局势判断，如果已经赢了，就直接将MAX设为一个最大的值（添加了一个非常小的随机因子）。然后给落子位置chooseX和chooseY赋值。最后取消这个落子，恢复棋盘。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/06.png?raw=true)  
&emsp;	 如果没有直接获胜，就进行下一步预测，也就是极小值层的搜索。先将MIN设为最大，变量cut1用于剪枝以及判断对方是否直接获胜时使用。同样遍历棋盘，如果五子且其周围有子，则为一个预估点，放置对手方的棋子。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/07.png?raw=true)  
&emsp;	然后判断对手方能否直接获胜且本方无法获胜。如果是则将MIN改为一个最小值。然后修改cut1的值，使这次极小层的搜索结束，还原棋盘，然后跳出循环。  
&emsp;	如果对手方没有直接取胜的点，继续向下进行一层极大值的搜索，MAX1也是先设为最小。变量Cut2用来剪枝。同样遍历棋盘，如果五子且其周围有子，则为一个预估点，放置本方的棋子。然后计算本方棋盘局势减去对方棋盘局势的分数（评估函数）。如果这个极大值小于这个分数，就重新为其赋值，最后还原这一步的棋盘。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/08.png?raw=true)  
&emsp; 	接下来是一个β剪枝  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/09.png?raw=true)  
&emsp;	  然后是第二层的极小值与第三层的极大值的一个倒推值的比较。如果极小值大，则重新赋值。最后恢复棋盘  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/10.png?raw=true)    
&emsp;	然后是一个α剪枝。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/11.png?raw=true)     
&emsp;	然后是最外一层的极大值和第二层的极小值的倒推值的比较。如果极大值小，则重新赋值，并且修改落子的坐标为当前预估落子的坐标。最后恢复棋盘。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/12.png?raw=true)       
&emsp;	以上就是AI落子的搜索过程。接下来是一些界面的设计和棋盘有关的代码。  
    
2. **人类落子的相关代码**  
&emsp;	人类落子的代码和AI的基本思想是相同的，不同的是AI要通过算法计算落子坐标，而人类是根据鼠标点击位置计算落子坐标。  
&emsp;	如下，形参x，y为鼠标点击的像素点的位置，我们通过计算将其转化为棋盘下标。并且这个下标处没有棋子时，放置人类的棋子。最后也要修改落子次序的状态。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/13.png?raw=true)       
    
 3. **与绘制棋盘，胜负判断有关的代码**    
&emsp;	有一个棋盘的board类，负责绘制棋盘，存储棋局信息，判定胜负等。这个类的构造函数如下。首先是声明一个二维数组用以存储棋局信息。变量now代表当前应为哪方下，前文已用到。变量runState代表当前棋局是已经结束还是在正常对弈。RED_PEN是一个自定义的红色画笔。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/14.png?raw=true)       
&emsp;	接下来是绘制棋盘和棋子。绘制棋盘的代码如下。hdc为窗口句柄，chooseX和chooseY为AI落子位置，默认为一个无效位置，我们要为AI的落子画一个红圈方便我们识别其先前落子位置。然后是两个for循环，绘制了棋盘的横线和竖线。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/15.png?raw=true)       
&emsp;	接下来是绘制棋子的过程。首先是获取系统的黑色和白色画刷。然后选择黑色画刷为天元点画一个小黑点。  
&emsp;	接下来遍历棋盘画棋子，每次遍历棋盘，如果落子为1，则画一个黑色圆圈，如果是-1则画一个白色圆圈。（先手为黑子，后手为白子）画完棋子后，再选择红色画笔根据chooseX和chooseY画一个圆圈作为AI落子提示。（如果chooseX和chooseY取值无效，这个红色圆圈是画不出来的，chooseX和chooseY默认值是一个无效值。）每次画完红圈后，要恢复默认的黑色画笔。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/16.png?raw=true)         
&emsp;	接下来是检测棋盘当中是否有一方（color方）已经获胜，其思想与AI评估棋盘局势基本相同，也是遍历棋盘，每个点朝四个方向延伸，观察是否有已成五子的线。如下是进行遍历。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/17.png?raw=true)         
&emsp;	然后是每个点的处理，先是定义朝四个方向变化的x，y坐标，已经用于计数的count。首先，判断当前的子是不是color颜色，如果不是直接过掉。如果是进行四次迭代，朝四个方向取四子，每有一子为color色，则count++，如果count最终为4，说明此线五子连珠，则sum++。最后如果sum大于0，则说明color色的棋子已有连成五子的线，则棋局结束，将代表棋局运行状态的变量runState改为false。最后返回sum值，可以根据sum值，判断某一方是否获胜。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/18.png?raw=true)         
&emsp;	下边是重置棋盘的reset函数，就是将整个二维数组置0，相关变量再度初始化。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/19.png?raw=true)         
    
4. **与MFC、鼠标点击相关的部分代码**  
&emsp;	本次实验是使用了win32程序，以及相关的MFC库，用以实现界面和鼠标监听等。先前已提到过少许界面方面的代码，下边将剩余代码展示。  
&emsp;	WM_PAINT是MFC中绘制窗口的消息，在窗口被创建时会被调用，也可以通过发送该条消息进行调用。每次调用时，先绘制棋盘，然后我们使用MessageBox语句弹出消息，询问是否选择先手，并根据其选择，为AI和人类的先手变量first赋值，以及下棋次序的变量now赋值。如果是AI先手，则先让AI落一子，然后再画一次棋盘。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/20.png?raw=true)            
&emsp;	消息WM_LBUTTONDOWN,代表鼠标左键被按下。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/21.png?raw=true)            
&emsp;	监听到此消息后，我们先获取点击的像素值。如果棋局尚未结束，我们将点击的位置做为形参传入到人类落子的函数中供其落子使用。然后绘制棋盘，判断落子之后人类是否获胜，如果获胜则弹出消息框提示，否则就继续进行。  
&emsp;	紧接着便是AI落子的代码，步骤与人类落子基本相同，不过无需获取鼠标点击位置。最终游戏结束时也是弹出消息框提醒。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/22.png?raw=true)          
&emsp;	如果检测到棋局已经结束，我们再度弹出消息框，询问是否再来一次。如果是，则重置棋盘，发送绘制棋盘的消息。否则就直接销毁窗口。  
![在这里插入图片描述](https://github.com/lianggx6/Tools/blob/master/TicTacToe_Cplusplus/23.png?raw=true)          
&emsp;	以上为整个五子棋项目设计的主要部分。  
  
### 结果分析
1. **实验环境** 
&emsp;本次实验使用DEV C++编译器，创建了DEV  win32项目程序，并使用了MFC的库进行界面和功能的设计。在代码上，使用了多文件编辑，共有main.cpp, board.h, board.cpp, AI.h, AI.cpp, human.h, human.cpp等文件，以及board、AI、human等自定义类。  
2. **算法性能**  
&emsp;本次实验采用极大极小搜索算法。搜索了三层（两层极大值，一层极小值）。每次搜索均需要遍历棋盘，棋盘大小为15*15，故三层搜索的复杂度为15的6次方。最后还需要遍历棋盘进行局势评估，所以最复杂的情况时，复杂度为15的8次方。这样每走一步棋需要大约25.6亿次左右的计算。对于目前的CPU处理速度，基本上在一秒钟左右下一步棋，可以满足与人正常对弈的时间需求。另外，由于使用了α-β剪枝算法，实际所需时间应当少于最复杂的时间。  
3. **实验结果** 
&emsp;实验结果上，与AI的对战次数较少，只与网上的一个AI进行过对战，无论先手后手均取得胜利。  
&emsp;	在与人类对战方面，虽然无法保证每次均胜利，但无论先后手，进攻性均比较强，能取得较多的胜利。  
4. **算法改进**
&emsp;对于本次的AI，三层的极大极小搜索基本可以在时间和对战性能上取得平衡。  
&emsp;	如果想要在对战性能上取得更为突破性的进步，一种情况是进行更深层次的搜索，但是时间和计算能力上已无法满足此需求，考虑可以使用多线程编程以加快计算。另一种情况是优化评估函数，根据网上的知识，大多数的五子棋AI在搜索上是大同小异的，真正产生差距的是评估函数的优化，所以改进对战能力的更好的方法是优化评估函数，使其更能适应五子棋的规则，这方面所需要的是对于五子棋的一个深入了解。  
  
### 主要参考  
1. 现如今五子棋AI水平的一些讨论
https://www.zhihu.com/question/27077916
2. AI设计参考
http://blog.csdn.net/pi9nc/article/details/10858411
3. 评估函数参考
http://blog.csdn.net/lihongxun945/article/details/50625267



