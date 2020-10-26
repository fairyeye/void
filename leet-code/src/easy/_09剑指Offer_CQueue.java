package easy;

import java.util.Stack;

/**
 * @author huapeng.zhang
 * @version 1.0
 * @date 2020/10/26 10:12
 */
public class _09剑指Offer_CQueue {

    private Stack<Integer> oneStack;
    private Stack<Integer> twoStack;


    /**
     * 一个栈只用来出栈  另一个只用来进栈
     */
    public _09剑指Offer_CQueue() {
        this.oneStack = new Stack<>();
        this.twoStack = new Stack<>();
    }

    public void appendTail(int value) {
        // 进栈
        oneStack.push(value);
    }

    public int deleteHead() {
        // 出栈 先判断第二个栈内是否有数据
        if (twoStack.empty()) {
            // 没有数据 把第一个栈内的数据放进来 最先进来的数据会放到最上面
            if (oneStack.empty()) {
                // 如果没有返回-1
                return -1;
            }
            while (!oneStack.empty()) {
                twoStack.push(oneStack.peek());
                oneStack.pop();
            }
        }
        int res = twoStack.peek();
        twoStack.pop();
        return res;
    }
}
