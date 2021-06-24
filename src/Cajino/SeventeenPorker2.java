package Cajino;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/*カード枚数は17枚。
 *スートはスペード、ハート、ダイヤ、クラブ
 *カードはJ,Q,K,Aの絵札に加えJokerが1枚とする
 *プレイヤーとディーラーが交互に手札を引く
 *手札が5枚になるまで繰り返す
 *一度だけ手札の交換が認められる
 *交換してよい枚数はお互いに3枚まで
 *配役によって勝敗が決まる
 *配役　強い順
 *ファイブカード(同数位の札4枚とジョーカーが揃った役)
 *ロイヤルストレートフラッシュ(一種類のスートで最も数位の高い5枚が揃った役)
 *(ルール上ストレートフラッシュは存在しない)
 *フォーカード(同数位の札4枚をすべてそろえた役)
 *フルハウス(同数位の札を3枚そろえ、残りの2枚もペアである役)
 *フラッシュ(一種類のスートだけで構成された役)
 *ストレート(5枚のカードの数位が連続して揃った役)
 *スリーカード(同数位の札を3枚そろえた役)
 *ツーペア(数位でそろえたペアが2組ある役)
 *ワンペア(数位でそろえたペアが1組ある役)
 */

public class SeventeenPorker2 {
	//山札に値を入れ、シャッフルするメソッド
	private static void shuffleDeck(List<Integer> deck) {

		//リストに1-16の連番と100(ジョーカー)を代入
		for(int i = 1; i <= 16; i++) {
			deck.add(i);
		}
		deck.add(100);

		//山札をシャッフル
		Collections.shuffle(deck);
	}

	//山札の値をランクに置き換えるメソッド
	private static String toRank(int number) {
		if(number == 100) {
			return "Joker";
		} else {
		switch(number % 4) {
		case 1:
			return "A";
		case 2:
			return "K";
		case 3:
			return "Q";
		case 0:
			return "J";
		default:
			return "例外です";
		}
		}
	}

	//山札の数をスートに置き換えるメソッド
	private static String toSuit(int cardNumber) {
			switch((cardNumber - 1)/4) {
			case 0:
				return "♠の";
			case 1:
				return "♥の";
			case 2:
				return "♦の";
			case 3:
				return "♣の";
			case 24:
				return "";
			default:
				return "例外です";
			}
	}

	//山札の値を”スートのランク”の文字列に変換するメソッド
	private static String  toDescription(int cardNumber) {
		String rank = toRank(cardNumber);
		String suit = toSuit(cardNumber);

		return suit + rank;
	}

	//ここからメイン
	public static void main(String[] args) {

		System.out.println("さぁ、闇のゲームの始まりだぜっ！！");
		System.out.println();

		//空の山札を作成
		List<Integer> deck = new ArrayList<>(17);

		//山札をシャッフル
		shuffleDeck(deck);

		//プレイヤー・ディーラーの手札リストを作成
		List<Integer> player = new ArrayList<>();
		List<Integer> dealer = new ArrayList<>();

		//空のリストを作成
		List<Integer> brank1 = new ArrayList<>();
		List<Integer> brank2 = new ArrayList<>();
		List<Integer> brank3 = new ArrayList<>();
		List<Integer> brank4 = new ArrayList<>();
		List<Integer> brank5 = new ArrayList<>();
		List<Integer> brankTC = new ArrayList<>();//スリーカード判定用
		List<Integer> brankTP = new ArrayList<>();//ツーペア判定用

		//プレイヤー・ディーラーがカードを5枚ずつ引く
		player.add(deck.get(0));
		dealer.add(deck.get(1));
		player.add(deck.get(2));
		dealer.add(deck.get(3));
		player.add(deck.get(4));
		dealer.add(deck.get(5));
		player.add(deck.get(6));
		dealer.add(deck.get(7));
		player.add(deck.get(8));
		dealer.add(deck.get(9));

		//山札の進行状況を記録する変数deckCountを定義
		int deckCount = 10;

		//playerのカードの容器を作る
		Collections.sort(player);
		int card1 = player.get(0);
		int card2 = player.get(1);
		int card3 = player.get(2);
		int card4 = player.get(3);
		int card5 = player.get(4);

		//dealerのカードの容器を作る
		Collections.sort(dealer);
		int card6 = dealer.get(0);
		int card7 = dealer.get(1);
		int card8 = dealer.get(2);
		int card9 = dealer.get(3);
		int card10 = dealer.get(4);

		//ポイント判定の容器を作る
		int playerPoint = 0;
		int dealerPoint = 0;

		//プレイヤーの手札を表示
		System.out.println("あなたの手札は");
		System.out.println(toDescription(card1) + "," + toDescription(card2) + "," + toDescription(card3) + "," + toDescription(card4) + "," + toDescription(card5) + "です");
		System.out.println();

		//playerの手札
		int playerArray[] = {card1, card2, card3, card4, card5};

		//手札の中から最小値を得る
		int intMin = 100;
		for(int i = 0; i < playerArray.length; i++) {
		if(intMin > playerArray[i]) {
			intMin = playerArray[i];
			}
		}

		//手札の中から最大値を得る
		int intMax = 0;
		for(int i = 0; i < playerArray.length; i++) {
		if(intMax < playerArray[i]) {
			intMax = playerArray[i];
			}
		}

		//playerの配役の判定

		//ジョーカーを持っている場合

		//ロイヤルストレートフラッシュ
		//ジョーカーがあり、Aが手札の最小値であるとき
		if(intMax == 100 && ((intMin == 1) || (intMin == 5) || (intMin == 9) || (intMin == 13))) {
			//最小値から4枚スートが同じ＋ジョーカーの配列を用意
			int rsfArray[] = {intMin, intMin + 1, intMin + 2, intMin + 3, intMax};
			//手札と上記配列が5枚とも一致しているかどうかの判定
			for(Integer a : playerArray) {
				for(Integer b : rsfArray) {
					if(a.equals(b)) {
						brank1.add(a);
					}
				}
			}
			if(brank1.size() == 5) {
				System.out.println("あなたの役はロイヤルストレートフラッシュです");
				playerPoint = 100;//ロイヤルストレートフラッシュの配点を加点
			} else {
				brankTC.add(1);//ロイヤルストレートフラッシュではなかったときにスリーカード判定用リストに1を追加
			}
			brank1.clear();//brank1リストは再利用するので空にしておく
		} else {
			brankTC.add(1);//このif構文に当てはまらなかった場合はスリーカード判定用リストに1を追加
		}

		//ファイブカード
		//ジョーカーがあり、最小値が♠のうちのどれかであるとき
		if(intMax == 100 && ((intMin == 1) || (intMin == 2) || (intMin == 3) || (intMin == 4))) {
			//最小値から4枚ランクが同じ＋ジョーカーの配列を用意
			int fcArray[] = {intMin, intMin + 4, intMin + 8, intMin + 12, intMax};
			//手札と上記配列が5枚とも一致しているかどうかの判定
			for(Integer a : playerArray) {
				for(Integer b : fcArray) {
					if(a.equals(b)) {
						brank1.add(a);
					}
				}
			}
			if(brank1.size() == 5) {
				System.out.println("あなたの役はファイブカードです");
				playerPoint = 150;//ファイブカードの配点を加点
			} else {
				brankTC.add(2);//ファイブカードではなかったときにスリーカード判定用リストに2を追加
			}
			brank1.clear();//brank1リストは再利用するので空にしておく
		} else {
			brankTC.add(2);//このif構文に当てはまらなかった場合はスリーカード判定用リストに2を追加
		}

		//フォーカード
		//ジョーカーがあり、最小値8以下であるとき(8以下でないと同じランクが手札内に3枚以上存在することができない)
		if(intMax == 100 && intMin <= 8) {
			//最小値からランクが同じ配列を用意(最小値が5以上のときは最小値-4もあり得る)
			int Array1[] = {intMin - 4, intMin, intMin + 4, intMin + 8, intMin + 12};
			//手札と上記配列が3枚一致しているかどうかの判定
			for(Integer a : playerArray) {
				for(Integer b : Array1) {
					if(a.equals(b)) {
						brank1.add(a);
					}
				}
			}
			if(brank1.size() == 3) {
				System.out.println("あなたの役はフォーカードです");
				playerPoint = 75;//フォーカードの配点を加点
			} else {
				//System.out.println("あなたの役はフォーカードではありません");
				brankTC.add(3);//フォーカードではなかったときにスリーカード判定用リストに3を追加
			}
			brank1.clear();//brank1リストは再利用するので空にしておく
		} else {
			brankTC.add(3);//このif構文に当てはまらなかった場合はスリーカード判定用リストに3を追加
		}

		//フルハウス
		//ジョーカーがあるとき
		if(intMax == 100) {
			//各ランクの配列と手札の配列がどれほど一致しているかの判定
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : playerArray) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
				//各ランクとの一致数が0か2になったときはフルハウスとなる
				if(((brank1.size() == 0) || (brank1.size() == 2)) && ((brank2.size() == 0) || (brank2.size() == 2)) && ((brank3.size() == 0) || (brank3.size() == 2)) && ((brank4.size() == 0) || (brank4.size() == 2))) {
				System.out.println("あなたの役はフルハウスです");
				playerPoint = 50;//フルハウスの配点を加点
			} else {
				brankTC.add(4);//フルハウスではなかったときにスリーカード判定用リストに4を追加
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();//brank1～4リストは再利用するので空にしておく
		}

		//ストレート
		//ジョーカーがあるとき
		if(intMax == 100) {
			//各ランクの配列と手札の配列がどれほど一致しているかの判定
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};

			for(Integer a : playerArray) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
			//ロイヤルストレートフラッシュかどうかの判定
			int rsfArray[] = {intMin, intMin + 1, intMin + 2, intMin + 3, intMax};
			for(Integer a : playerArray) {
				for(Integer b : rsfArray) {
					if(a.equals(b)) {
						brank5.add(a);
					}
				}
			}
			//各ランクが1枚であり、ロイヤルストレートではないときはストレートとなる
			if(((brank1.size() == 1) && (brank2.size() == 1) && (brank3.size() == 1) && (brank4.size() == 1)) && (brank5.size() != 5)) {
				System.out.println("あなたの役はストレートです");
				playerPoint = 25;//ストレートの配点を加点
			} else {
				brankTC.add(5);//ストレートではなかったときにスリーカード判定用リストに5を追加
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();
			brank5.clear();//brank1～5リストは再利用するので空にしておく
		}

		//スリーカード
		//ジョーカーがあるとき
		if(intMax == 100) {
			//今までのすべての配役に当てはまらなかったとき(スリーカード判定用リストに1～5の5つが追加されているとき)
			if(brankTC.size() == 5) {
				System.out.println("あなたの役はスリーカードです");
				playerPoint = 10;//スリーカードの配点を加点
			} else {
			}
			brankTC.clear();//スリーカード判定用リストは再利用するので空にしておく
		}


		//ジョーカー未所持の場合

		//フォーカード
		//ジョーカーがないとき
		if(intMax != 100) {
			//最小値から4枚ランクが同じ配列を用意する
			int Array1[] = {intMin, intMin + 4, intMin + 8, intMin + 12};
			for(Integer a : playerArray) {
				for(Integer b : Array1) {
					if(a.equals(b)) {
						brank1.add(a);
					}
				}
			}
			//手札と上記配列が4枚一致しているかどうかの判定
			if(brank1.size() == 4) {
				System.out.println("あなたの役はフォーカードです");
				playerPoint = 75;//フォーカードの配点を加点
			} else {
				brankTP.add(1);//フォーカードではなかったときにツーペア判定用リストに1を追加
			}
			brank1.clear();//brank1リストは再利用するので空にしておく
		}

		//フルハウス
		//ジョーカーがないとき
		if(intMax != 100) {
			//各ランクの配列と手札の配列がどれほど一致しているかの判定
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : playerArray) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
				//各ランクとの一致数が0か2か3になったときはフルハウスとなる
				if(((brank1.size() == 0) || (brank1.size() == 2) || (brank1.size() == 3)) && ((brank2.size() == 0) || (brank2.size() == 2) || (brank2.size() == 3)) && ((brank3.size() == 0) || (brank3.size() == 2) || (brank3.size() == 3)) && ((brank4.size() == 0) || (brank4.size() == 2) || (brank4.size() == 3))) {
				System.out.println("あなたの役はフルハウスです");
				playerPoint = 50;//フルハウスの配点を加点
			} else {
				brankTP.add(2);//フルハウスではなかったときにツーペア判定用リストに2を追加
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();//brank1～4リストは再利用するので空にしておく
		}

		//スリーカード
		//ジョーカーがないとき
		if(intMax != 100) {
			//各ランクの配列と手札の配列がどれほど一致しているかの判定
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : playerArray) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
			//各ランクとの一致数が3か1以下になったときはスリーカードとなる
				if(((brank1.size() == 3) || (brank1.size() <= 1)) && ((brank2.size() == 3) || (brank2.size() <= 1)) && ((brank3.size() == 3) || (brank3.size() <= 1)) && ((brank4.size() == 3) || (brank4.size() <= 1))) {
				System.out.println("あなたの役はスリーカードです");
				playerPoint = 10;//スリーカードの配点を加点
			} else {
				brankTP.add(3);//スリーカードではなかったときにツーペア判定用リストに3を追加
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();//brank1～4リストは再利用するので空にしておく
		}

		//ワンペア
		//ジョーカーがないとき
		if(intMax != 100) {
			//各ランクの配列と手札の配列がどれほど一致しているかの判定
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : playerArray) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : playerArray) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
				//各ランクとの一致数が2か1かつ0ではないときはワンペアとなる
				if((((brank1.size() == 2) || (brank1.size() == 1)) && (brank1.size() != 0)) && (((brank2.size() == 2) || (brank2.size() == 1)) && (brank2.size() != 0)) && (((brank3.size() == 2) || (brank3.size() == 1)) && (brank3.size() != 0)) && (((brank3.size() == 2) || (brank3.size() == 1)) && (brank3.size() != 0)) && (((brank4.size() == 2) || (brank4.size() == 1)) && (brank4.size() != 0))) {
				System.out.println("あなたの役はワンペアです");
				playerPoint = 1;//ワンペアの配点を加点
			} else {
				brankTP.add(4);//ワンペアではなかったときにツーペア判定用リストに4を追加
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();//brank1～4リストは再利用するので空にしておく
		}

		//ツーペア
		//ジョーカーがないとき
		if(intMax != 100) {
			//今までのすべての配役に当てはまらなかったとき(ツーペア判定用リストに1～4の4つが追加されているとき)
			if(brankTP.size() == 4) {
				System.out.println("あなたの役はツーペアです");
				playerPoint = 5;//ツーペアの配点を加点
			} else {

			}
			brankTP.clear();//ツーペア判定用リストは再利用するので空にしておく
		}

		brankTC.clear();//スリーカード判定用リストは再利用するので空にしておく
		brankTP.clear();//ツーペア判定用リストは再利用するので空にしておく


		//プレイヤーがカードの交換をするフェーズ
		System.out.println();
		while(true) {
			System.out.println("3枚までカードの交換を行えます。");
			System.out.println("カードの番号を入力してEnterを入力して下さい。交換しない場合は0を入力しEnter");
			//交換したいカードを入力させる
			Scanner scan = new Scanner(System.in);
			String str = scan.next();
			//0を入力した場合は抜ける
			if ("0".equals(str)) {
				break;
			//0でなかった場合は対応したカードに新たなカードを代入する
			} else if (!("0".equals(str))) {

				//String型の1～5を容器に入れる
				String h1 = "1";
				String h2 = "2";
				String h3 = "3";
				String h4 = "4";
				String h5 = "5";

				//入力された文字列中の1～5の文字を検索する(文字列が見つからなかった場合は-1を返す)
				int result1 = str.indexOf(h1);
				int result2 = str.indexOf(h2);
				int result3 = str.indexOf(h3);
				int result4 = str.indexOf(h4);
				int result5 = str.indexOf(h5);

				//文字列に1が入力された場合
				if (result1 != -1) {
					card1 = deck.get(deckCount);
					deckCount++;
				}
				//文字列に2が入力された場合
				if (result2 != -1) {
					card2 = deck.get(deckCount);
					deckCount++;
				}
				//文字列に3が入力された場合
				if (result3 != -1) {
					card3 = deck.get(deckCount);
					deckCount++;
				}
				//文字列に4が入力された場合
				if (result4 != -1) {
					card4 = deck.get(deckCount);
					deckCount++;
				}
				//文字列に5が入力された場合
				if (result5 != -1) {
					card5 = deck.get(deckCount);
					deckCount++;
				}
				break;
			} else {
				System.out.println("交換できるカードは3枚までです。もう一度入力してください");
				//3枚以上は交換できないようにする機構をいれたい
			}
		}

		//交換後プレイヤーの手札の表示
		System.out.println("あなたの手札は");
		System.out.println(toDescription(card1) + "," + toDescription(card2) + "," + toDescription(card3) + "," + toDescription(card4) + "," + toDescription(card5) + "です");
		System.out.println();

		int playerArray2[] = {card1, card2, card3, card4, card5};
		intMin = 100;
		for(int i = 0; i < playerArray2.length; i++) {
			if(intMin > playerArray2[i]) {
				intMin = playerArray2[i];
				}
			}

		intMax = 0;
		for(int i = 0; i < playerArray2.length; i++) {
			if(intMax < playerArray2[i]) {
				intMax = playerArray2[i];
				}
			}


		//交換後playerの配役の判定

		//ジョーカーを持っている場合
		//ロイヤルストレートフラッシュ
		if(intMax == 100 && ((intMin == 1) || (intMin == 5) || (intMin == 9) || (intMin == 13))) {
			int rsfArray[] = {intMin, intMin + 1, intMin + 2, intMin + 3, intMax};
			for(Integer a : playerArray2) {
				for(Integer b : rsfArray) {
					if(a.equals(b)) {
						brank1.add(a);
					}
				}
			}
			if(brank1.size() == 5) {
				System.out.println("あなたの役はロイヤルストレートフラッシュです");
				playerPoint = 100;
			} else {
				brankTC.add(1);
			}
			brank1.clear();
		} else {
			brankTC.add(1);
		}

		//ファイブカード
		if(intMax == 100 && ((intMin == 1) || (intMin == 2) || (intMin == 3) || (intMin == 4))) {
			int fcArray[] = {intMin, intMin + 4, intMin + 8, intMin + 12, intMax};
			for(Integer a : playerArray2) {
				for(Integer b : fcArray) {
					if(a.equals(b)) {
						brank1.add(a);
					}
				}
			}
			if(brank1.size() == 5) {
				System.out.println("あなたの役はファイブカードです");
				playerPoint = 150;
			} else {
				brankTC.add(2);
			}
			brank1.clear();
		} else {
			brankTC.add(2);
		}

		//フォーカード
		if(intMax == 100 && intMin <= 8) {
			int Array1[] = {intMin - 4, intMin, intMin + 4, intMin + 8, intMin + 12};
			for(Integer a : playerArray2) {
				for(Integer b : Array1) {
					if(a.equals(b)) {
						brank1.add(a);
					}
				}
			}
			if(brank1.size() == 3) {
				System.out.println("あなたの役はフォーカードです");
				playerPoint = 75;
			} else {
				brankTC.add(3);
			}
			brank1.clear();
		} else {
			brankTC.add(3);
		}

		//フルハウス
		if(intMax == 100) {
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : playerArray2) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
				if(((brank1.size() == 0) || (brank1.size() == 2)) && ((brank2.size() == 0) || (brank2.size() == 2)) && ((brank3.size() == 0) || (brank3.size() == 2)) && ((brank4.size() == 0) || (brank4.size() == 2))) {
				System.out.println("あなたの役はフルハウスです");
				playerPoint = 50;
			} else {
				brankTC.add(4);
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();
		}

		//ストレート
		if(intMax == 100) {
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};

			for(Integer a : playerArray2) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
			int rsfArray[] = {intMin, intMin + 1, intMin + 2, intMin + 3, intMax};
			for(Integer a : playerArray2) {
				for(Integer b : rsfArray) {
					if(a.equals(b)) {
						brank5.add(a);
					}
				}
			}

			if(((brank1.size() == 1) && (brank2.size() == 1) && (brank3.size() == 1) && (brank4.size() == 1)) && (brank5.size() != 5)) {
				System.out.println("あなたの役はストレートです");
				playerPoint = 25;
			} else {
				brankTC.add(5);
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();
			brank5.clear();
		}

		//スリーカード
		if(intMax == 100) {
			if(brankTC.size() == 5) {
				System.out.println("あなたの役はスリーカードです");
				playerPoint = 10;
			} else {
			}
			brankTC.clear();
		}


		//ジョーカー未所持の場合
		//フォーカード
		if(intMax != 100) {
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : playerArray2) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
				if((brank1.size() == 4) || (brank2.size() == 4) || (brank3.size() == 4) || (brank4.size() == 4)) {
				System.out.println("あなたの役はフォーカードです");
				playerPoint = 75;
			} else {
				brankTP.add(1);
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();

		}

		//フルハウス
		if(intMax != 100) {
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : playerArray2) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
				if(((brank1.size() == 0) || (brank1.size() == 2) || (brank1.size() == 3)) && ((brank2.size() == 0) || (brank2.size() == 2) || (brank2.size() == 3)) && ((brank3.size() == 0) || (brank3.size() == 2) || (brank3.size() == 3)) && ((brank4.size() == 0) || (brank4.size() == 2) || (brank4.size() == 3))) {
				System.out.println("あなたの役はフルハウスです");
				playerPoint = 50;
			} else {
				brankTP.add(2);
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();
		}

		//スリーカード
		if(intMax != 100) {
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : playerArray2) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
				if(((brank1.size() == 3) || (brank1.size() <= 1)) && ((brank2.size() == 3) || (brank2.size() <= 1)) && ((brank3.size() == 3) || (brank3.size() <= 1)) && ((brank4.size() == 3) || (brank4.size() <= 1))) {
				System.out.println("あなたの役はスリーカードです");
				playerPoint = 10;
			} else {
				brankTP.add(3);
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();
		}

		//ワンペア
		if(intMax != 100) {
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : playerArray2) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : playerArray2) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
				if((((brank1.size() == 2) || (brank1.size() == 1)) && (brank1.size() != 0)) && (((brank2.size() == 2) || (brank2.size() == 1)) && (brank2.size() != 0)) && (((brank3.size() == 2) || (brank3.size() == 1)) && (brank3.size() != 0)) && (((brank3.size() == 2) || (brank3.size() == 1)) && (brank3.size() != 0)) && (((brank4.size() == 2) || (brank4.size() == 1)) && (brank4.size() != 0))) {
				System.out.println("あなたの役はワンペアです");
				playerPoint = 1;
			} else {
				brankTP.add(4);
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();
		}

		//ツーペア
		if(intMax != 100) {
			if(brankTP.size() == 4) {
				System.out.println("あなたの役はツーペアです");
				playerPoint = 5;
			} else {
			}
			brankTP.clear();
		}

		brankTC.clear();
		brankTP.clear();

		System.out.println("-------------------------");
		System.out.println("-------------------------");

		//ディーラーの役判定

		System.out.println("ディーラーの手札は");
		System.out.println(toDescription(card6) + "," + toDescription(card7) + "," + toDescription(card8) + "," + toDescription(card9) + "," + toDescription(card10) + "です");
		System.out.println();

		//手札の中から最小値を得る
		int dealerArray[] = {card6, card7, card8, card9, card10};
		intMin = 100;
		for(int i = 0; i < dealerArray.length; i++) {
		if(intMin > dealerArray[i]) {
			intMin = dealerArray[i];
			}
		}

		//手札の中から最大値を得る
		intMax = 0;
		for(int i = 0; i < dealerArray.length; i++) {
		if(intMax < dealerArray[i]) {
			intMax = dealerArray[i];
			}
		}

		//playerの配役の判定

		//ジョーカーを持っている場合
		//ロイヤルストレートフラッシュ
		if(intMax == 100 && ((intMin == 1) || (intMin == 5) || (intMin == 9) || (intMin == 13))) {
			int rsfArray[] = {intMin, intMin + 1, intMin + 2, intMin + 3, intMax};
			for(Integer a : dealerArray) {
				for(Integer b : rsfArray) {
					if(a.equals(b)) {
						brank1.add(a);
					}
				}
			}
			if(brank1.size() == 5) {
				System.out.println("ディーラーの役はロイヤルストレートフラッシュです");
				dealerPoint = 100;
			} else {
				brankTC.add(1);
			}
			brank1.clear();
		} else {
			brankTC.add(1);
		}

		//ファイブカード
		if(intMax == 100 && ((intMin == 1) || (intMin == 2) || (intMin == 3) || (intMin == 4))) {
			int fcArray[] = {intMin, intMin + 4, intMin + 8, intMin + 12, intMax};
			for(Integer a : dealerArray) {
				for(Integer b : fcArray) {
					if(a.equals(b)) {
						brank1.add(a);
					}
				}
			}
			if(brank1.size() == 5) {
				System.out.println("ディーラーの役はファイブカードです");
				dealerPoint = 150;
			} else {
				brankTC.add(2);
			}
			brank1.clear();
		} else {
			brankTC.add(2);
		}

		//フォーカード
		if(intMax == 100 && intMin <= 8) {
			int Array1[] = {intMin - 4, intMin, intMin + 4, intMin + 8, intMin + 12};
			for(Integer a : dealerArray) {
				for(Integer b : Array1) {
					if(a.equals(b)) {
						brank1.add(a);
					}
				}
			}
			if(brank1.size() == 3) {
				System.out.println("ディーラーの役はフォーカードです");
				dealerPoint = 75;
			} else {
				brankTC.add(3);
			}
			brank1.clear();
		} else {
			brankTC.add(3);
		}

		//フルハウス
		if(intMax == 100) {
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : dealerArray) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
				if(((brank1.size() == 0) || (brank1.size() == 2)) && ((brank2.size() == 0) || (brank2.size() == 2)) && ((brank3.size() == 0) || (brank3.size() == 2)) && ((brank4.size() == 0) || (brank4.size() == 2))) {
				System.out.println("ディーラーの役はフルハウスです");
				dealerPoint = 50;
			} else {
				brankTC.add(4);
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();
		}

		//ストレート
		if(intMax == 100) {
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : dealerArray) {
			for(Integer A : ArrayA) {
				if(a.equals(A)) {
					brank1.add(a);
				}
			}
			}
			for(Integer a : dealerArray) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
			int rsfArray[] = {intMin, intMin + 1, intMin + 2, intMin + 3, intMax};
			for(Integer a : dealerArray) {
				for(Integer b : rsfArray) {
					if(a.equals(b)) {
						brank5.add(a);
					}
				}
			}

			if(((brank1.size() == 1) && (brank2.size() == 1) && (brank3.size() == 1) && (brank4.size() == 1)) && (brank5.size() != 5)) {
				System.out.println("ディーラーの役はストレートです");
				dealerPoint = 25;
			} else {
				brankTC.add(5);
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();
			brank5.clear();
		}

		//スリーカード
		if(intMax == 100) {
			if(brankTC.size() == 5) {
				System.out.println("ディーラーの役はスリーカードです");
				dealerPoint = 10;
			} else {
			}
			brankTC.clear();
		}


		//ジョーカー未所持の場合
		//フォーカード
		if(intMax != 100) {
			int Array1[] = {intMin, intMin + 4, intMin + 8, intMin + 12};
			for(Integer a : dealerArray) {
				for(Integer b : Array1) {
					if(a.equals(b)) {
						brank1.add(a);
					}
				}
			}
			if(brank1.size() == 4) {
				System.out.println("ディーラーの役はフォーカードです");
				dealerPoint = 75;
			} else {
				brankTP.add(1);
			}
			brank1.clear();
		}

		//フルハウス
		if(intMax != 100) {
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : dealerArray) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
				if(((brank1.size() == 0) || (brank1.size() == 2) || (brank1.size() == 3)) && ((brank2.size() == 0) || (brank2.size() == 2) || (brank2.size() == 3)) && ((brank3.size() == 0) || (brank3.size() == 2) || (brank3.size() == 3)) && ((brank4.size() == 0) || (brank4.size() == 2) || (brank4.size() == 3))) {
				System.out.println("ディーラーの役はフルハウスです");
				dealerPoint = 50;
			} else {
				brankTP.add(2);
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();
		}

		//スリーカード
		if(intMax != 100) {
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : dealerArray) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
				if(((brank1.size() == 3) || (brank1.size() <= 1)) && ((brank2.size() == 3) || (brank2.size() <= 1)) && ((brank3.size() == 3) || (brank3.size() <= 1)) && ((brank4.size() == 3) || (brank4.size() <= 1))) {
				System.out.println("ディーラーの役はスリーカードです");
				dealerPoint = 10;
			} else {
				brankTP.add(3);
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();
		}

		//ワンペア
		if(intMax != 100) {
			int ArrayA[] = {1, 5, 9, 13};
			int ArrayK[] = {2, 6, 10, 14};
			int ArrayQ[] = {3, 7, 11, 15};
			int ArrayJ[] = {4, 8, 12, 16};
			for(Integer a : dealerArray) {
				for(Integer A : ArrayA) {
					if(a.equals(A)) {
						brank1.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer K : ArrayK) {
					if(a.equals(K)) {
						brank2.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer Q : ArrayQ) {
					if(a.equals(Q)) {
						brank3.add(a);
					}
				}
			}
			for(Integer a : dealerArray) {
				for(Integer J : ArrayJ) {
					if(a.equals(J)) {
						brank4.add(a);
					}
				}
			}
				if((((brank1.size() == 2) || (brank1.size() == 1)) && (brank1.size() != 0)) && (((brank2.size() == 2) || (brank2.size() == 1)) && (brank2.size() != 0)) && (((brank3.size() == 2) || (brank3.size() == 1)) && (brank3.size() != 0)) && (((brank3.size() == 2) || (brank3.size() == 1)) && (brank3.size() != 0)) && (((brank4.size() == 2) || (brank4.size() == 1)) && (brank4.size() != 0))) {
				System.out.println("ディーラーの役はワンペアです");
				dealerPoint = 1;
			} else {
				brankTP.add(4);
			}
			brank1.clear();
			brank2.clear();
			brank3.clear();
			brank4.clear();
		}

		//ツーペア
		if(intMax != 100) {
			if(brankTP.size() == 4) {
				System.out.println("ディーラーの役はツーペアです");
				dealerPoint = 5;
			} else {
			}
			brankTP.clear();
		}

		brankTC.clear();
		brankTP.clear();


		//勝敗判定
		System.out.println();
		System.out.println("-----------------");
		System.out.println("勝敗は・・・！？");
		System.out.println("-----------------");
		System.out.println();

		//playerPointとdealerPointを比較して勝敗判定を行う
		if (playerPoint > dealerPoint) {
			System.out.println("あなたの勝利です！");
		} else if (playerPoint == dealerPoint) {
			System.out.println("引き分けです");
		} else if (playerPoint < dealerPoint) {
			System.out.println("あなたの負けです...");
		} else {
			System.out.println("勝敗判定エラー");
		}

		/*これからやりたいこと
		 * 手札の交換制限を3枚までにする
		 * ディーラーに手札交換の機構を入れたい
		 * プレイヤーの手札交換の前にディーラーの交換情報をいれたい
		 * 引き分けだった場合のランク髙い方が勝ちルールの採用はできるか？
		 * オブジェクト指向をいれて簡潔にしたい
		 */

		System.out.println();
		System.out.println("おわりでーす");

	}

}
