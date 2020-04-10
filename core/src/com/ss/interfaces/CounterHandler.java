package com.ss.interfaces;

public interface CounterHandler {
    public void CounterHandler(String ctx);
    public boolean ActionHandler(ActionCompleteListener listener);
}

/*

class CounterButton(x,y,stage, algin, CounterHandle handler) {
    CounterButton() {
        this.handler = handler;
        addListener(new InpuListener() {
            touchDown() {
                if (c > 0){
                    c--;
                    handler.CounterHandler();
                }
                else
                {
                    c = 3;
                    handler.ActionHandler();
                }
            }
        })
    }
}

*/

/*
 lclass GameScrene  {
    new Counterbtn(stage, x, y, align.center, new CounterHandle() {

    })

    CounterHandler() {
        this.board.switchMode();
    }

    ActionHandler() {
        Popcat.platform.ShowAds();
    }
 }
 */