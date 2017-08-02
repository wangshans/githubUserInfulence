//package hits;
//
//
///**
//
//*
//
//* @author You Wang
//
//*/
//public class Hitsnet {
//
//
//
///**
//
//* 正向图
//
//*/
//	private ImmutableGraph g;
//
///**
//
//* 反向图
//
//*/
//
//	private ImmutableGraph ig;
//
///**
//
//* 日志
//
//*/
//
//	private final Logger logger;
//
///**
//
//* 结点数目
//
//*/
//
//	private int numNodes;
//
///**
//
//* 权威分数
//
//*/
//
//	private double[] authorityScores;
//
///**
//
//* 中心分数
//
//*/
//
//	private double[] hubScores;
//
///**
//
//* 两次权威分数之差绝对值的和
//
//*/
//
//	private double authorityNorm;
//
///**
//
//* 两次中心分数之差绝对值的和
//
//*/
//
//	private double hubNorm;
//
///**
//
//* 迭代次数
//
//*/
//
//	private double numIter = 0;
//
///**
//
//* 获取中心差值
//
//* @return
//
//*/
//
//	public double getHubNorm() {
//
//		return hubNorm;
//
//	}
//
///**
//
//* 获取权威差值
//
//* @return
//
//*/
//
//	public double getAuthorityNorm() {
//
//		return authorityNorm;
//
//	}
//
///**
//
//* 获取权威分数
//
//* @return
//
//*/
//
//	public double[] getAuthorityScores() {
//
//	return  authorityScores;
//
//	}
//
///**
//
//* 获取中心分数
//
//* @return
//
//*/
//
//public double[] getHubScores() {
//
//return hubScores;
//
//}
//
///**
//
//* 构造函数
//
//* @param g 要计算的Web图
//
//*/
//
//public Hitsnet(ImmutableGraph g) {
//
//this.g = g;
//
//ig = Transform.transpose(g);
//
//numNodes = g.numNodes();
//
//authorityScores = new double[numNodes];
//
//hubScores = new double[numNodes];
//
//double is = 1.0 / numNodes;
//
//for (int i = 0; i < numNodes; i++) {
//
//authorityScores[i] = is;
//
//hubScores[i] = is;
//
//}
//
//logger = Logger.getLogger(HITS.class);
//
//}
//
///**
//
//* 设定初始权威分数
//
//* @param scores
//
//*/
//
//public void setInitialAuthorityScores(double[] scores) {
//
//if (scores.length != numNodes)
//
//throw new IllegalArgumentException("array length mismatch");
//
//this.authorityScores = scores;
//
//}
//
///**
//
//* 设定初始中心分数
//
//* @param scores
//
//*/
//
//public void setInitialHubScores(double[] scores) {
//
//	if (scores.length != numNodes)
//
//		throw new IllegalArgumentException("array lenght mismatch");
//
//	this.hubScores = scores;
//
//}
//
///**
//
//* 迭代中的一步
//
//*/
//
//	public void step() {
//
//		System.out.println("iter " + ++numIter);
//	
//		authorityNorm = 0;
//	
//		hubNorm = 0;
//	
//		NodeIterator nit = g.nodeIterator();
//	
//		NodeIterator init = ig.nodeIterator();
//	
//		double[] as = new double[numNodes];
//	
//		double[] hs = new double[numNodes];
//	
//		while(nit.hasNext() && init.hasNext()) {
//	
//			int i = nit.nextInt();
//	
//			int j = init.nextInt();
//	
//			assert (i == j);
//	
//			LazyIntIterator it = init.successors();
//	
//			as[i] = 0;
//	
//			int k;
//	
//			while ((k = it.nextInt()) != -1) {
//	
//				as[i] += hubScores[k];
//	
//				}
//	
//			hs[i] = 0;
//	
//			it = nit.successors();
//	
//			while ((k = it.nextInt()) != -1) {
//	
//				hs[i] += authorityScores[k];
//	
//			}
//	
//		}
//
//		// 归一化处理
//
//		normalize(as);
//	
//		normalize(hs);
//
//		authorityNorm = computeNorm(authorityScores, as);
//		
//		hubNorm = computeNorm(hubScores, hs);
//		
//		authorityScores = as;
//		
//		hubScores = hs;
//		
//		System.out.println("authority norm: " + authorityNorm);
//		
//		System.out.println("hub norm: " + hubNorm);
//
//	}
//
///**
//
//* 归一化
//
//* @param a
//
//*/
//
//private void normalize(double[] a) {
//
//double s = 0;
//
//for (double d : a)
//
//s += d;
//
//for (int i = 0; i < a.length; i++)
//
//a[i] /= s;
//
//}
//
///**
//
//* 计算绝对差和
//
//* @param a
//
//* @param b
//
//* @return
//
//*/
//
//private double computeNorm(double[] a, double[] b) {
//
//if (a.length != b.length)
//
//throw new IllegalArgumentException("array length mismath");
//
//double norm = 0;
//
//for (int i = 0; i < a.length; i++) {
//
//norm += Math.abs(a[i] - b[i]);
//
//}
//
//return norm;
//
//}
//
///**
//
//* 一直迭代，知道达到最大次数限制
//
//* @param iter 最大迭代次数
//
//*/
//
//public void stepUntil(int iter) {
//
//while (iter-- > 0)
//
//step();
//
//}
//
///**
//
//* 一直迭代，直到达到规定的停止基准
//
//* @param stopNorm 停止基准
//
//*/
//
//public void stepUntil(double stopNorm) {
//
//while (authorityNorm > stopNorm || hubNorm > stopNorm)
//
//step();
//
//}
//
//}