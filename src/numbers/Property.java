package numbers;

import java.util.function.LongFunction;

public enum Property {
  EVEN(Main::isEven),
  ODD(Main::isOdd),
  BUZZ(Main::isBuzz),
  DUCK(Main::isDuck),
  PALINDROMIC(Main::isPalindromic),
  GAPFUL(Main::isGapful),
  SPY(Main::isSpy),
  SUNNY(Main::isSunny),
  SQUARE(Main::isSquare),
  JUMPING(Main::isJumping),
  HAPPY(Main::isHappy),
  SAD(Main::isSad);

  public final LongFunction<Boolean> func;

  Property(LongFunction<Boolean> func) {
    this.func = func;
  }
}
