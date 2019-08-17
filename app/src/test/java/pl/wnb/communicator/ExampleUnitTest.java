package pl.wnb.communicator;

import org.bouncycastle.openpgp.PGPException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;

import pl.wnb.communicator.util.GenerateKeyUtil;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private final String privMy = "-----BEGIN PGP PRIVATE KEY BLOCK-----\n" +
            "Version: BCPG v1.60\n" +
            "\n" +
            "lQdGBF0ObRwDEADFkVIqwWuTrd71a6346M8TvMdw/jX8CAUQJTmUVOpdo0re50Ss\n" +
            "5zcvWN1FEprRQSfY5+uipoBeS7U6lQneJdDTp92nkodhPzb//o4WZ8z9KFoggkpd\n" +
            "c4kMfd2BWmCt0gzCUT5f1aAZRASu2sVRjaCzyDviy8Qa9e/rdKQrmDChyWs8j4Nr\n" +
            "kBMHy9KIuGiQAFWuUEMixgmwpYey6tdgzEvOqXi9Zq5uP0VRkZlsSo2ix4JKmf/h\n" +
            "eoyQQOBt+f6PBaRBx8Cgnd3jxmqa7pNCqqk1d6ocj8hj1zIpuNYl5lbmgVOVurmI\n" +
            "yg9SlUZhFTnGd+4hLaCoc4iQLWWgRY/h+WozOxKfm0SqjCE7HWGtE8ydS4pPhDk7\n" +
            "U/p6VJm1ICc/KfhuzzQOXT+laI1SlB4mnt3Htz2NoMWxln3awOnOD7PTTY5+Io6t\n" +
            "Pqys5wCwv5gJAh6J44zV8UwHfChZ4TVKuFASA0FdjolGmKm6QNuQN5D1eNhKpuWH\n" +
            "d0rfcVH4T5t+pv+JYkDauEJehkpO/uCqrUHHiZUGmJ8R7rRDYXw08nOK8zuJ5yyO\n" +
            "fF5RT9QV06J46VOWeGRjGmouIuWrCVv1kOItzsTPyxhibAudfFiFjIBmkU2odn32\n" +
            "lDk637kvHcMW/dEGmlKUDNZ7vfAfjBCYzjKpQX+AuMkU2c3Rh7BelEWviQARAQAB\n" +
            "/gkDCJLpqbgOvS7QwOagFkQDUCB9bxl6RNDoDzNrSJrPIp4aomCxTzU/gD5CSyzG\n" +
            "E+DDOFa8O/ZnmCy1jsqNlPEn8OBmnEYtdQDvs0+rvLDbfOVPc3b44IHJ9XHBQ89L\n" +
            "Cssaezxy7pTcBfnN6uER3LtlP7BtnnxE9yz7fjEr0bkAU8SWN81XBW9CJdySPt00\n" +
            "DUei1jTtNlYK681+E+QYAunQxi3r3CHyMahaMPYKMFHx64aHLgiG/z74hJQDFHGQ\n" +
            "EAbr4jYKGpHHM1kJ3fxxLboE45gPlIj1zvuwJVD5paW67LTX/5APDWNrpuz1Il32\n" +
            "z4V+3NW/MB23p0VrieT/kRpXtUV0FACzu63YqU2rk+WeMhPLU1LSFPu5JmtKp8I2\n" +
            "oywXyuMTSZ866MqsskIjqmgxkgE1JY0E4sBpm5C0GfpSfRDDwebkdWoJyhHKwsq9\n" +
            "tWeD0J1eOP6k+Q6WKhviqKIsFwrRUhsZ8f6AJGqZesxo3Dw/438Al84NTuf+453d\n" +
            "q+pIBD+QPwjWqT0v+4kClYDoLND4MFXlOV7xUtpJwu4x6u3SR+w0+04wbNzGn2Ro\n" +
            "cUzbivP+qkURCIZha7oOQdDwnPxmB2vr/vHWk5L8qALbFwuV4L9CMtSFXiO8ziUf\n" +
            "Wr8U64ycc2dRujcjJTAIoodJCM48A549aJH5GwvQpw6HBri0oAvV9TSaovatzhse\n" +
            "HRIocjXBN02q6ti8CT5pMrK4eQWdC3/QdGT73OQedJmwc9Fi1WvV5jqmnm5nNnov\n" +
            "DUZsArWtuh5FPIoPbyLLpWes0GxXPONB/PDUGs78I3OOq9Aakz/tPW0ERUog98hd\n" +
            "fFNGrdkCD0p9Ls2incq0lcaQpCXYIGXkph+uSypGZre055zDaECXx9EPlSdfrdC4\n" +
            "YH2KYg9QgPqtSqsVVC3s49MADL5H6GEznItCEUyLvivU6AXqC19IskQE6sXefO2H\n" +
            "vroXypTlAuOyy8MQNHnWbsunLZY8bszsWuwVAOrgyxXtt/IrX9KAJFWDN1ORvsJi\n" +
            "5KTBV87IVBWoo+43CPnN1aPyDgVdWyiUMUwhy7RG8gge/O4pWWqdbzAuIDvqE3+D\n" +
            "gUrNyIePV8qUX5TXjRznWb2s5NTZL/n7vbfC9KzaOK4DWTIfYvYSpL35TLwsdj0V\n" +
            "vsQ9yTyTSkRKu+oIjNC0YwbI5yppKyC/vzsgtxwheeM7kNKRfUMxkIpbAgSplC9V\n" +
            "tRZJTSLG9bmJKwmDS6ktDi8vGVVX+9XxP3vMvAF3HfY15HRo1+dEb8pJdl1ClyIE\n" +
            "HrN2gtSKPOxlB59C6Jvde4LbYF4mPyLC491X3c2WYC6GQeNU7Bb3afSba7HluPoX\n" +
            "c/cPxAMhXZoUbfzywP/LppILNpTMxxxVVdkKgzuO2+325FWPr6rriXpzSOTmcMyu\n" +
            "FwIumGYRx4j1O0VuIHI4ovZMzfQsVD+iXddFsNMeIr6e2pmy62QBO1bJ/PdCyKyh\n" +
            "077HhDyGQ9KjAKI3WbhvXmQB7UbKpa2e9z6xbuNLvj/0uejt5mxEF0y0cagKrwbP\n" +
            "QEEmxuXlButK0raEaV/h+stnYTFdIGJ6TjkqWy+0yHVbR9A1MSURuiF3HtFCFHIS\n" +
            "r7fL8KPRaVobDxkuHrDJOJwycUi43bkAnXCvPh1gzRE88rTjnVgSD0IodhafpLSY\n" +
            "S+E2wi/wx9vUc7TOEdanX4cVdZJ3uLhlTVuyBRXDSyPYynD8MXaGNT5wxdM7hIRU\n" +
            "3bmOj46YlN4NTP9qBxzMXD9ox2VtgALzYgvWRF0H47RZRGArJGqeVOG0DG5hbWUg\n" +
            "PGVtYWlsPokCKAQTAwIAEgUCXQ5tKQIbAwILCQIVCgIeAQAKCRDIU7aBNW6wBdyq\n" +
            "D/4spegpK8U3Kr5UjS5YvuvwUgVVWIuxGYQw5RvqZDj3pZApMmQUpNbir+EPeKrI\n" +
            "0utw5R3ljTeF96g5Ur9F8YFjXKA2PZ25ey4h4bTwOCZ7CSMCWZLmWgsTY6c26/uH\n" +
            "Z4OKaQ+N4SbAGUFSzB6WZCU/B82sH8g3EHqNMKb8yvgw+oMY9LoZ2FhBzLsIqEg0\n" +
            "hl8MkOH6D8ds0/DkksQCPHJtTbopVdRPZ74N/uaQozUleVewS3ccEJrUCVeIvPOE\n" +
            "2Hndacg73/0arGW1Rl0RywWTAKW9ewZ7/ez1eTmWZAFrGNUbhOsuWHh+xBu2yE+1\n" +
            "eYRFBpL4ybiOne3h8Av4Fou193qnQf77b4KuiGk3Px6nveC8Bd+l39wx6tAXL5/W\n" +
            "IbqVZmGQrBNsQofaEp6X2+Nogq0OzxykeR+GgHLHeRMYLCCZnEEAa/TSdkeyvTyK\n" +
            "2Wk5ejuOE8W6afwy++ouBh6XPL/iMVnRbc7E7fdoFtEy9O22EnSSrA5aBz5mb0lR\n" +
            "LZWnknJOs2ypt3h7y2peINlgcr5lEmlQAgTkcze9npWWjK6zGbDc39P4itcciqYr\n" +
            "W5BNo3R7WkyPQDgI6F1L33HykaPhuySJlVsM/k+BnpppfllhSfv2wA8P3LkRelUp\n" +
            "KceMjC97DBDUaZG55Mxdzj4mfFVgLdgA0h4X8wQDgEJ3R50HRgRdDm0cARAAk3pN\n" +
            "D3hvFiqh9HCw3QaMylX60GwHvc/38FJ5NuFFVbUDEyYznrWqVtoFuacAZ1RYp5YM\n" +
            "ZbZIgpJkjtvM/on8/TNh5iwIOO/KK+Q7VK5rrzIVb2fyzV26wEnQSMknQsVJNrlf\n" +
            "v5jmZrYLQzNOHHKN9OyonwzHblp8yHy/IFPrFnKPvIhJEJ8g8uN7ICETkQ1y28Hu\n" +
            "nfZId0cQ/oaS3GnTB+/KeAtKxPhZgj+6WWePROFNKvE5mebFAp/dWw1EuAr6rFI4\n" +
            "BHFcby0SIepIHpnI0BNnsAR10ZG1TBgEQhYCgNbEINM6dPsQqNm0wE3qDE0ss7Du\n" +
            "yHBYZtwI8bHNiqPBTVh0rg/4dk2EX4ZI1dLHDiGeHJWwaLKLu6i7Vpst5aSZIagC\n" +
            "WjOXwRNXpQlHJa9IpSU2pR8dU19aiunLezEH+Db8czeQQeoVC1IPVEvuOy7qvGOX\n" +
            "xHZh8AfPDstwsvlaDkSokU+zVO1uDOevaHwr32SU4Hurp+xHwWQPQdCK547b2xeY\n" +
            "VeZY3ts0wg+9/xJYpE9OidTGbvep9tQzHeOiiRjLTcOTvd8cj2eMOQD972keRhLS\n" +
            "zIAcxqn5oAqQmpyapvDnwFFGIoCpOTrMZlooKVvKYtA1Fz2XJWq7IENNtw0qzRHw\n" +
            "S5uLRD5OlacvsI28XSpPN1z4Zoh4fOQvQjTh1PkAEQEAAf4JAwiS6am4Dr0u0MAG\n" +
            "OJxtt/nFy94T6Xj3/FmCzmJdBK9PwscUx071uw789xezPDZAyk+wAyX6MXfqFdcU\n" +
            "iGw9uuygt3GKGQ90xFT1op164e1ntDPNXpDSSjV5Om8xurwhBAIfdbwgouXO7/Cl\n" +
            "349Y/fbIqKn4E44xaC5vnNIl9wNGYIye9lR8F7z94KsnUwsjZqlLRx4BLhMfV6Uq\n" +
            "MiTpDdB9J767rXNrz1zq1sPzgG2jbTSy8vHdBUGo0BNyrqllB10Z/k+MaEZk/Iql\n" +
            "OeLs3vWMIzS3foUsLu9FSgEH0R3pT0yAWKE7syx8Kd8kqcYtVe0fmMio5kWFtihP\n" +
            "OuP7cIZqoyX/NZCmra6jYYpB8FjgTpPbzHitFOV35UlHVwoY5vqUZVcwRwTa6ig8\n" +
            "SymoFT1jngOPcaHyvJ93V+4qSTAJGEk3RmGhOkiH4fyVPv8L4MXJiA+MktZUxv0a\n" +
            "/hPfdx62WsWuhgzM/foyvVo8Yf3URjJZdpihcEgCkHTAbaQoPK++X9R4C0gjCmYJ\n" +
            "HG9z27EpBRLkoXoMsFdCcbPz24jwZlQqHg8s4CO/om8StVrwAPuJShOTvphwPTD5\n" +
            "eZqhiojBy7H+ZrjMJ+4KjbJRYQbstIO6mH4I8pzEcAlIunNHcnLfCTvMdqVLHZLB\n" +
            "m51p7yZF85xng+l6+y3aeBieSoQyiSFmtPnjc1t5LD/b2IcNHmJSUmrVHnsvqeYq\n" +
            "ugqL3rPQy3xOkvUYR1yiTLLXdzSmX/meMNg91jo4e4undROpUES+4nzsfRosiFq8\n" +
            "E2EJF9T0MiguV6tfNq40Fm5A8HGAiN+YykUmNFzIVXg6OxnlQ9OM+ixswjWryA6D\n" +
            "HJrSnMGzwtJeAUkUvxijpT8lqZvhtjmJAKBy3ZBqpA2zTGpmsW/l8t1WskphmKCD\n" +
            "TDDM+BlvkBhdr6ZM6RUwtB6/3WyRCtdsEfpYnJtL79A75nm8XPNKyvkc+Mqfw4c6\n" +
            "f1nhKIJ11e8pZoRP+rpmkcrKwKS4wxq2FMZ4gW7obIOilhvpGVfORp60wvpAQB77\n" +
            "Jye1nm+CFPqdefZ1D4IfdvN2lDD7iJgb7E+KSOZs4P3prCbazoPhFd8HiF9S5Ucy\n" +
            "RQFScYYIAwpKDyACN5rrXeGYgdawQI9wGoa/fr59dE7BywPCbPOU06+hTbWPz6/c\n" +
            "mD6J324Y3zopGbyLVBZ8/keFkzW60W0fKFZv/mNmFIVMwD+fJ173hsG+LTiYlogW\n" +
            "sg7lQoENbrwLSIk0kh3sZ/RPHZgvshdEbF5VJGlE+1V7UBWQSNpylEJZP/Grq4Ze\n" +
            "NG7+iCnZpa3zUWwH+IchXPqeFEnksty+XgzLBFJ3SjLwcjzOrmKx3H9qZuBLoN3s\n" +
            "Iu5ovnlwW5mIkjaWqCYSqus5+nM5MyYdiTHztIdBvWCAiqSjF+WolqVMG1vgL89Y\n" +
            "SthEilKkFtcRxZbBQPSLktZKbIz71FWipKsISrIyVZRLM+FTKxSMCum6bcY8K4jH\n" +
            "+SnoCRR72dk/U0EmEjYnFGqDH1xwOvG0FIGfjqpaClI/RaH+cd4Uo8Jjy2WNoQXv\n" +
            "/5eeY5sgWES1gUACQS55vUZJIwHGRTgBJssFkm5LVNoADRH3tKrkdg/XuFaLchrf\n" +
            "Of0gulGHbXFr2aBOkMHiq3VgWMyVlrwZtnfz+aCkNqPAksqsDJp+k4vUKj0jL2mb\n" +
            "l4mPjO2P8pWKOWjGkcVJiyfPniJf0RvkFY4uu8hbLcAPOhyCAn8VuFZg49NaxxgI\n" +
            "LBHXOGaNg5GCEW8zjywL65jgcfvxkhCZoMz9iQIfBBgDAgAJBQJdDm0pAhsMAAoJ\n" +
            "EMhTtoE1brAFSk0P/0LmmVMCash4SlRPTseTgNMZwHYiJwv8VU6n5ttxgS54o3yc\n" +
            "Q1C0vCrcNIS9ZzuzSRoIgdUlidksUqA9M//+oI4ahuVBo2eiTt69RV93ScNJhKWU\n" +
            "rtCbrtcvdoWtRJ/C74/liUmJNJ8sih0ZUDoZ3SEL8WYRFC6t3m5HwAI3QhEgHKO1\n" +
            "F3MULZMOxnYR+NdEdxlK8OwaCKLcMs9IwD4cJKLoSaxVTuLLiy5sEIHLV4Mj92kD\n" +
            "PDa0/905qfFPu5IqGfqf/2lUr0yWBUUhqwdJhNkkleAb75ef1sSgAUgC54eWaWSZ\n" +
            "DS9QFzBw1ei98ERo/q6CCmZfHEfpHarWqZWkCJsDnAMPrMBBbPfcON/T2a7yUp+B\n" +
            "5DQGu3r4SjzYBL3SOiK9jzVUl78R+8q65juuZLzdhFi3D/fe5HNazapIRIDdM6OD\n" +
            "yx5mehaDgp6uHjUVGZ7ADDLbD4r5O0kyv6qA65ox8VObhS/6rVOXbgN+qV7XdIJM\n" +
            "GCrRad92xu+4CFlE8Q4fHC9e7VmSz8xeNFksE2q18Dbt9k2GtNwOk/9Yi60k+5mX\n" +
            "z6MyLX9yDu/CT2jesIGaDei7TEVhXsbQAFdyIHxHdImbWt0JNS1rNlQP5N+b7nG1\n" +
            "I1vwdgA5ICMLgY4uuV627/L3kXLGSwSmOIQS7b9Ml/b68/ybTZz9srpGcHIS\n" +
            "=HnQi\n" +
            "-----END PGP PRIVATE KEY BLOCK-----\n";

    private final String pubMy = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
            "Version: BCPG v1.60\n" +
            "\n" +
            "mQINBF0ObRwDEADFkVIqwWuTrd71a6346M8TvMdw/jX8CAUQJTmUVOpdo0re50Ss\n" +
            "5zcvWN1FEprRQSfY5+uipoBeS7U6lQneJdDTp92nkodhPzb//o4WZ8z9KFoggkpd\n" +
            "c4kMfd2BWmCt0gzCUT5f1aAZRASu2sVRjaCzyDviy8Qa9e/rdKQrmDChyWs8j4Nr\n" +
            "kBMHy9KIuGiQAFWuUEMixgmwpYey6tdgzEvOqXi9Zq5uP0VRkZlsSo2ix4JKmf/h\n" +
            "eoyQQOBt+f6PBaRBx8Cgnd3jxmqa7pNCqqk1d6ocj8hj1zIpuNYl5lbmgVOVurmI\n" +
            "yg9SlUZhFTnGd+4hLaCoc4iQLWWgRY/h+WozOxKfm0SqjCE7HWGtE8ydS4pPhDk7\n" +
            "U/p6VJm1ICc/KfhuzzQOXT+laI1SlB4mnt3Htz2NoMWxln3awOnOD7PTTY5+Io6t\n" +
            "Pqys5wCwv5gJAh6J44zV8UwHfChZ4TVKuFASA0FdjolGmKm6QNuQN5D1eNhKpuWH\n" +
            "d0rfcVH4T5t+pv+JYkDauEJehkpO/uCqrUHHiZUGmJ8R7rRDYXw08nOK8zuJ5yyO\n" +
            "fF5RT9QV06J46VOWeGRjGmouIuWrCVv1kOItzsTPyxhibAudfFiFjIBmkU2odn32\n" +
            "lDk637kvHcMW/dEGmlKUDNZ7vfAfjBCYzjKpQX+AuMkU2c3Rh7BelEWviQARAQAB\n" +
            "tAxuYW1lIDxlbWFpbD6JAigEEwMCABIFAl0ObSkCGwMCCwkCFQoCHgEACgkQyFO2\n" +
            "gTVusAXcqg/+LKXoKSvFNyq+VI0uWL7r8FIFVViLsRmEMOUb6mQ496WQKTJkFKTW\n" +
            "4q/hD3iqyNLrcOUd5Y03hfeoOVK/RfGBY1ygNj2duXsuIeG08DgmewkjAlmS5loL\n" +
            "E2OnNuv7h2eDimkPjeEmwBlBUswelmQlPwfNrB/INxB6jTCm/Mr4MPqDGPS6GdhY\n" +
            "Qcy7CKhINIZfDJDh+g/HbNPw5JLEAjxybU26KVXUT2e+Df7mkKM1JXlXsEt3HBCa\n" +
            "1AlXiLzzhNh53WnIO9/9GqxltUZdEcsFkwClvXsGe/3s9Xk5lmQBaxjVG4TrLlh4\n" +
            "fsQbtshPtXmERQaS+Mm4jp3t4fAL+BaLtfd6p0H++2+CrohpNz8ep73gvAXfpd/c\n" +
            "MerQFy+f1iG6lWZhkKwTbEKH2hKel9vjaIKtDs8cpHkfhoByx3kTGCwgmZxBAGv0\n" +
            "0nZHsr08itlpOXo7jhPFumn8MvvqLgYelzy/4jFZ0W3OxO33aBbRMvTtthJ0kqwO\n" +
            "Wgc+Zm9JUS2Vp5JyTrNsqbd4e8tqXiDZYHK+ZRJpUAIE5HM3vZ6Vloyusxmw3N/T\n" +
            "+IrXHIqmK1uQTaN0e1pMj0A4COhdS99x8pGj4bskiZVbDP5PgZ6aaX5ZYUn79sAP\n" +
            "D9y5EXpVKSnHjIwvewwQ1GmRueTMXc4+JnxVYC3YANIeF/MEA4BCd0e5Ag0EXQ5t\n" +
            "HAEQAJN6TQ94bxYqofRwsN0GjMpV+tBsB73P9/BSeTbhRVW1AxMmM561qlbaBbmn\n" +
            "AGdUWKeWDGW2SIKSZI7bzP6J/P0zYeYsCDjvyivkO1Sua68yFW9n8s1dusBJ0EjJ\n" +
            "J0LFSTa5X7+Y5ma2C0MzThxyjfTsqJ8Mx25afMh8vyBT6xZyj7yISRCfIPLjeyAh\n" +
            "E5ENctvB7p32SHdHEP6Gktxp0wfvyngLSsT4WYI/ullnj0ThTSrxOZnmxQKf3VsN\n" +
            "RLgK+qxSOARxXG8tEiHqSB6ZyNATZ7AEddGRtUwYBEIWAoDWxCDTOnT7EKjZtMBN\n" +
            "6gxNLLOw7shwWGbcCPGxzYqjwU1YdK4P+HZNhF+GSNXSxw4hnhyVsGiyi7uou1ab\n" +
            "LeWkmSGoAlozl8ETV6UJRyWvSKUlNqUfHVNfWorpy3sxB/g2/HM3kEHqFQtSD1RL\n" +
            "7jsu6rxjl8R2YfAHzw7LcLL5Wg5EqJFPs1Ttbgznr2h8K99klOB7q6fsR8FkD0HQ\n" +
            "iueO29sXmFXmWN7bNMIPvf8SWKRPTonUxm73qfbUMx3jookYy03Dk73fHI9njDkA\n" +
            "/e9pHkYS0syAHMap+aAKkJqcmqbw58BRRiKAqTk6zGZaKClbymLQNRc9lyVquyBD\n" +
            "TbcNKs0R8Eubi0Q+TpWnL7CNvF0qTzdc+GaIeHzkL0I04dT5ABEBAAGJAh8EGAMC\n" +
            "AAkFAl0ObSkCGwwACgkQyFO2gTVusAVKTQ//QuaZUwJqyHhKVE9Ox5OA0xnAdiIn\n" +
            "C/xVTqfm23GBLnijfJxDULS8Ktw0hL1nO7NJGgiB1SWJ2SxSoD0z//6gjhqG5UGj\n" +
            "Z6JO3r1FX3dJw0mEpZSu0Juu1y92ha1En8Lvj+WJSYk0nyyKHRlQOhndIQvxZhEU\n" +
            "Lq3ebkfAAjdCESAco7UXcxQtkw7GdhH410R3GUrw7BoIotwyz0jAPhwkouhJrFVO\n" +
            "4suLLmwQgctXgyP3aQM8NrT/3Tmp8U+7kioZ+p//aVSvTJYFRSGrB0mE2SSV4Bvv\n" +
            "l5/WxKABSALnh5ZpZJkNL1AXMHDV6L3wRGj+roIKZl8cR+kdqtaplaQImwOcAw+s\n" +
            "wEFs99w439PZrvJSn4HkNAa7evhKPNgEvdI6Ir2PNVSXvxH7yrrmO65kvN2EWLcP\n" +
            "997kc1rNqkhEgN0zo4PLHmZ6FoOCnq4eNRUZnsAMMtsPivk7STK/qoDrmjHxU5uF\n" +
            "L/qtU5duA36pXtd0gkwYKtFp33bG77gIWUTxDh8cL17tWZLPzF40WSwTarXwNu32\n" +
            "TYa03A6T/1iLrST7mZfPozItf3IO78JPaN6wgZoN6LtMRWFextAAV3IgfEd0iZta\n" +
            "3Qk1LWs2VA/k35vucbUjW/B2ADkgIwuBji65Xrbv8veRcsZLBKY4hBLtv0yX9vrz\n" +
            "/JtNnP2yukZwchI=\n" +
            "=F882\n" +
            "-----END PGP PUBLIC KEY BLOCK-----\n";


    private final  String pubThey = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
            " Version: OpenPGP.js v4.4.10\n" +
            " Comment: https://openpgpjs.org\n" +
            " \n" +
            " xsFNBF0OaE4BEADAsysv3R4KyA8Ff2bzjPAX+fIBjDeRr3Dyeg7OwFR/kmXU\n" +
            " GMiImAHXFBD/5zi7GBlxwPoelyGa92UQC+TFsp3iqRN5X/SiOmdW8BZeabYR\n" +
            " DEMzj++uSMAPAs6wVtnSz8vBNoov6JfyZNPv1BQLCqGmum8lFYgW0WULBQ7d\n" +
            " 5lVZeXXwQ3W7Sa7rV4Q7zzVfR3X7+b+lcFMkK22IIqZRuqV3+D5N2qefaT1J\n" +
            " gWN1XFYs0xH7ToDWUrfjdZzW42gKK7MyCBxfug//+8VdETp92Cpe+AjD/ran\n" +
            " 45va9cdEHeI24B0dY5UEUh0St5jTlhYv/b7Cba5h8na8khXEuQXKr/6Sc6C1\n" +
            " 2H/KjseqEDj4gvU1oa21jPI0m6E+JN1X1ujn6Z2uhyVzTYNRDL26IcuACnjx\n" +
            " IULIjUwB6nnHamNz+vWs3qvS8slYLlquyAVJyjWnpRfIi4SzmluxJcyv7rOP\n" +
            " xdy2YydgSCIxpYM8Mb9S5p3ELYehdHijOzlOHIrPszi9axveTGmjTPDreLju\n" +
            " vyILOlhWLDGVU4VGf8MFM+JMfzyzcu75jOtlLnQXnBPbvEBUpfJrU/LsjoCq\n" +
            " RcSy6EW55hMrgWEUQJ/6rKzOxS3BREJVEKZv8tlEdMYfNuFsFgb8zzXjJHQL\n" +
            " TcBApCHEHRcu8S16wf5GdqbvgIoRHDDXwjJUfQARAQABzRR0ZXN0IDx0ZXN0\n" +
            " QHRlc3QuY29tPsLBdQQQAQgAHwUCXQ5oTgYLCQcIAwIEFQgKAgMWAgECGQEC\n" +
            " GwMCHgEACgkQAkX2wITUuxQrCRAAgPL9jDYfZme+afOai8PORdlAcgl9mYSO\n" +
            " 5rWc5UdT/adbJZ0GDlLsT3zy2Z15g0ebhB0kyLisbqLh/3Xic/ZbPYI/t06o\n" +
            " cAzebT2QDDVP+Y5MJ2Shf+f7IaVMiYu6BCvqi0aV20AwjJ8mPdpdgKu9fKcW\n" +
            " 4p8By9yvnhLaZSgy7kW/9IreVS64uCkIQXXhoWVyT16WQBUmLHU94IzUofVC\n" +
            " 8jaTnFeHkDSJPz5YMW1xbnvhoaiHCNigDfKyqdwb1ps7EC/tOP9MHzEd6GHr\n" +
            " ZbD7avPObLYRCoHvSiyHx8Pao4RTIkIvJqUkYiRczRArjrluJ1VDO/QoC4g/\n" +
            " erOls2AAsoKwR3+suqREWsPk1RrE39Y4C2+I9iAsoPlTuSZ8Y23YRj0aHSuP\n" +
            " HPrge7KCmbHh+D7dqCAPDsCVhCmSheHcCqchF3B5Hb9lt7qI/qR5MbPXrZFY\n" +
            " VziCFMh2WP9Rh4VDbTmXddNcMvQUvYRAZFR2U62xibFr9RipFKFXDFIEJKW+\n" +
            " jlzMksfK9vvqu5qZMYN+qeNJbkCXN3dpHPEkNVaNmXu4YZhILNXiWGPvNJjg\n" +
            " FSG+Hf4Ts4wbfUVdtAPQHgnhY4DVV/EDtZo35DGw+wv3JKUGh0Z+HQuNthoF\n" +
            " Q9ZqJcRIINwbCGeUiYI9WBnurKpxqDseY+jeYo7wSETjGOWQvsPOwU0EXQ5o\n" +
            " TgEQAJjlgmJzV4mKnEgk1eESXBKYJsCr1S5OSZdEUqNqoBAml0myJGRezm02\n" +
            " sP5uSWhGtzy2AJNggGEA8GBQJsh+ayaNixKUmXnJ8RdUafrmbTi63S+P4Iyn\n" +
            " Pd0xyKB1I4b6vu6yxZ5USJorpaO9wqynY2983fWRv+TKDDsFroLed+PbqbZK\n" +
            " KFnnrIuHIXosu9cfj3abwUppWVcyP0Q0Dm2/jOu7BqUsWvG4Rtg9X7m/mXDG\n" +
            " 3mVU5ka3OJlrSGe28RLbWYsFlH5vqFII7EcE0a9m1wnwbdUPU0odm2aNBDhX\n" +
            " +CL6k48JW17Yxqq2+3v1Yae3k0oVprXkGrU1+Jf1v/AC0NsXiLU0vnvMghGv\n" +
            " 457u+N1I4QnyXLvzxAqge0LsZf5U42lvSV9tQfwvP9Dec5Q1zdPdlbyPSOq9\n" +
            " pnIF//siJ0xEif1CuAfs1GpE1nIUO4SiGjkZkrWU+55TnmAqeZeL+9IdJRxg\n" +
            " 9JRgNe6xbHPavC1mzBiGVNRF02OQGPqqeXhWSKfEDlBDgCs95CctbYe2zn6i\n" +
            " HfcVMTRv4cfDZfgZuVapVgl6QsrVu0896fPWQ0SgfA9TmP3CF+VzOI+wvp4s\n" +
            " eMmypwTifdExAAYPotenwDEQ4Ic84YrkrwGqUkClMzhIY7xAw1Jg8wknxIIn\n" +
            " asOuV6u4txWaPwcjNp811kFKvHmfABEBAAHCwV8EGAEIAAkFAl0OaE4CGwwA\n" +
            " CgkQAkX2wITUuxRy8g/9Gpjt3tAZUF8d/uZuK85txAonIdXeFtIee5wsACmP\n" +
            " xBQmSyU7Q2x7WbJSmd2sU5YyqwK4QYhTCYsz/um4scMEjbVt+69fYhUr3A20\n" +
            " WHELVvy5AA+FscbvyIJb6vR7YKcHwo6dk9CvSykLKf8dXeqDdlDdyRXJLSqQ\n" +
            " 3ii92M9OVioxaEb6idOe9X7YYVaUlvUuE0MICe7W7hKnTzXaHYEvysevUTmv\n" +
            " fjTO1s3TwuzwfoBtXk4Vk8WF2TJSVc5clUfxJpWL8/zGY83V/a97fOhJ8MaX\n" +
            " JvM0MhlWPA/qnKme3GgRyXxpkp1kAFpZY/MyjFofdj88HUSdikFTCfwFNMxa\n" +
            " pPI+d9cT7gApg4jdcUaB9WitQXsrUOhbZ17b8st/qBcdf7CCVqxSl01aCj7N\n" +
            " bvbpRe6SyKUGWLLtw+VUrE2S3Z8SgcCQR1sMTxCWww9tYAsNpKrs3Pqz3zfw\n" +
            " y6dHp+sSC06DKITTpxPAE62Lmupn+/ZTy6FWiWSjK/HEChRpTO3Oc+lepOw1\n" +
            " CNDANHEfvvV4eZW5v6+Se0iQULE0680+3e3181RkyMDqcou7gGADrxHIPxX1\n" +
            " 2EH+sCqPqKF9/n13XuZLw+mZ2PKlqELVvu7wJ/Rq/jrf3E3C54PSwyrrHiyc\n" +
            " E3PLO0LF/Ld7Ybw+ySSKJTcKAMU3zLn4Tt6Bx2xKGoI=\n" +
            " =LIJN\n" +
            " -----END PGP PUBLIC KEY BLOCK-----\n";

//    private final String pubThey = "-----BEGIN PGP PUBLIC KEY BLOCK-----\n" +
//            "Version: OpenPGP.js v4.4.10\n" +
//            "Comment: https://openpgpjs.org\n" +
//            "\n" +
//            "xsFNBF0PtJ0BEADTbYrmeC8xS/wo2Q/ed5PB6c4D7Ab6D+oeCDvFOs7Tvff9\n" +
//            "zdMDzJc0a8gtQ+7Owv1/NEQT8DeO40kvzK5+/v2VDLIIC60we/Cy025Vc5EW\n" +
//            "uByuPS/rbtoVhMHSeb2P4CMRK7udLg2grs/cf3gn1GIZYhhI38Ijw2YfYKr/\n" +
//            "sdqGQOgTuFFIT4HhNKxKBULlG/wyDvUzZC6OL8DK/vSXeFMDnaf7x1VD4DuY\n" +
//            "8M3UkJBDI96xlb3JqQB0JhmjStbMN8V/P2KDmwbNdDrnZwZQdaqYYHLs9yyB\n" +
//            "Wr/YpLiGahXkeMXL343qQ1epEWakFZpxxMtmoX7wcUdjCEnsWbo5lHNkumWj\n" +
//            "L0Qz4Oluypgy6+RnyVIPSb180xhgZCUws+A/srGH/+38kPWY/TZg/JyM4jET\n" +
//            "yISH1HJIDat4NZmmJKfU7kQWdHLarztkqFnjpVF2mriivr1GDPHLkBX8LNS+\n" +
//            "UFESnbXIkd2mWYxuSD5qpw8OqYikY8RUYI5JkOwrqFTB+vH52uFpMMdRdh1h\n" +
//            "hs5yZSzZkTKy35tKt7eV2Wq15yHjf/ZF+r9Ixzk6UZxH9ebs91CmOF0ngBH7\n" +
//            "UO30SKGyDKtA5GzXGhusnsc/tkPMGkyFCKAqu5RELGp33w/fuEn1uNzzu/uM\n" +
//            "uKzH2Sho61CcUlDK5nERaC2XRIwMwZ6Ve0WT7QARAQABzRRhc2RmIDxhc2Rm\n" +
//            "QGFzZGYuY29tPsLBdQQQAQgAHwUCXQ+0nQYLCQcIAwIEFQgKAgMWAgECGQEC\n" +
//            "GwMCHgEACgkQKiDt+PSG5SC9phAAshfiYqtUx86Vvo1wPfuAQDX0+Yr5brar\n" +
//            "ggAzyjOnwIPX3f7O6AjEtA2YSGWLB1+ZyTRGIBqsa4OL1tRrvA7mgNqJkSYd\n" +
//            "3HFYIJE6xq3gKInbBI+wrPKrsaF6zpHdVpPOtFMZVAH2j818jHc0ysHxMZ9Z\n" +
//            "EgJASLWNU1WDgLFYyVozZY3maSQwY1Db36d2nlyChYrcgF4ofEREfo3y9ogQ\n" +
//            "zzAWMdFIHeQ/NSWIEY3LLcYLPN+NhYUBwwUh19l8ojbQiFT1EkrQ2GSNZs43\n" +
//            "PgGKuLRI+UudBEpplPUkxqB4mIYYdLyazGfD1ti/QGQmVj717laNEqGThoyw\n" +
//            "E5+TZdEODuNno4iXWaYl6VP6j3l2j2481n4OknKaKWtl7kVFStlzlSmJDsEP\n" +
//            "FygCkcG0LXzjIBSs6DirwwVFsXqby8gkwWfVLWB0H3lHCPbyK59dOooOIAsm\n" +
//            "XYPIVQXLal9bFXDI3yahhauAapkoJ6e3AK5jt5zFDK9lfYKtXJkBvV8kxJGd\n" +
//            "2iFUSKJg773oO7SNSbaHttynn3OV02gOo5y6JBSUaXyOCBkNLBtxdQXUsnez\n" +
//            "LIpkRuWOedl0JYooWhGerGMI9QKM6BfOLK+pH72bxJp4kWG6ExPcses6ZDgR\n" +
//            "LR07oEMsztvGpkEhG1NcdhIoE1dQ6IRCgONQjJU5PVGuzX7vQdjOwU0EXQ+0\n" +
//            "nQEQAO5bFtpeXifL77A3ffpxL5xhM9ql/VCPbTQkzFDI1iergdNPdR2r1Dib\n" +
//            "mSHnen/RhQZPvkBExMRNmkOFXjwBfhHsHuPTDVTbHOGDouTmnmDk8nMKmgTx\n" +
//            "dyile33KZfCn2JpCTxa9GYgDff4/26IUrGBqhDsM+VVmwpxZ87UspeA5bWPv\n" +
//            "TTWgXbY6MJHcISihUH9T5rkyIhh0b45XoSI8eCwss5y+idwhdS5zN8DPMgI5\n" +
//            "ISLO7N3ssoidWDlybQtkaaIXv7EWXE+l8Qm82yNg1/LyKjn8EcODhwRimkEQ\n" +
//            "VIO7gOsov+wWPvD7Rf6hFG2MIL/rl9hp5OUcRZKz4MmwQrYXyJjBYCwkPwZW\n" +
//            "D3xlptG9NOr8PH7riWidipdeDtlDQ0RYpXEOAhyF7hmYsfPZd79tEaDOSHsl\n" +
//            "WiLdxeJsV7nB1Tm84WiSkAeouWfzI6dDkRgWRI2I0Vm8T6TeEDQmFSnduQW4\n" +
//            "vSgiVdxHyLs8P8VUuHijblmpla8KjmtlNNcflvkBxuFZMRnkWUJwbJx2e27f\n" +
//            "pruG8Tz0vVPGvVAyWtvE3IsbLyGEjA545LSMRpb1ZbgPr2Zn3bcNHqqCxPGp\n" +
//            "0s3F7re6wRC0rHLTHLojq2YmOKTUA+BDYrIWaahTlKSMrAYX0n+Lf1Ub6XkI\n" +
//            "GbcaJR8ddpZjdXGbLYVR8jGEfhENABEBAAHCwV8EGAEIAAkFAl0PtJ0CGwwA\n" +
//            "CgkQKiDt+PSG5SCEWRAAgMetaSlXoQ99lIkKgeF1uKCk6SgC5tsGC+T03Ytw\n" +
//            "VRQPkLMpXQP141c7pXg4ibVfLv+w5ec+JpMula96HDwEMbW8q4lLmSpK7FU4\n" +
//            "d7zwJdv3LFFIkU+FBdaxDQSQ52C5N6xE2mIjnudpBnOmidmHv36sqGHQCUqE\n" +
//            "DJnsz6oqb6TRN7VfEDe2VHWLRzBqvQQVxR2ua4KWkKJjionOoG+4KlwJrHGJ\n" +
//            "VyKH99BfNRG4hcW35/pBB/AYGWR2UWxW69KcinGqgVZgZuS8MCHrzZZ+ygCB\n" +
//            "B0yMYf/67IANJYXLgCciBUdv1vVgIw265YXcKUwVJmsxDKgGsx41D8Xoa9HP\n" +
//            "Wle+bGQ6cF1kWsLbRnCJ2lEjU9NAqckKRb8F77xjka8ORYmsgfFKyuI/uj+u\n" +
//            "u9hL2AuP/SonIAQ2DKP/tvMmuPEkuwuPmndZ601/9vlTJSjKzfy5/Ma1qC0r\n" +
//            "8vRry9S5PbgOsMHo2gEHyL8I9gyCOUs75D87qKrgVd7Dni5pO763bwB693a0\n" +
//            "ukQOfefn5N/mYu91YE4Dg5g8soz1FHxIIEi2Ddf50i/ip8wBvFiEU1pk3vxu\n" +
//            "bmbO6cARMW/4MsE71CZKJ0q1pXIQiUUqFsgrrpBuwvw4JLnsD3+VyF/ZTIro\n" +
//            "sn1adtP9azbh0N4aAX6I1TwxwdpuoolkUlRE3lFF7xQ=\n" +
//            "=YZJK\n" +
//            "-----END PGP PUBLIC KEY BLOCK-----\n";


    private final String msg = "-----BEGIN PGP MESSAGE-----\n" +
            "Version: OpenPGP.js v4.4.10\n" +
            "Comment: https://openpgpjs.org\n" +
            "\n" +
            "wcFMA275g0pai1IfAQ/+LqRO+piptIHQM5OVwp766HGm2aBdNKHIL0sCXJcC\n" +
            "v7nCjI0/arlfAxC6NFeysT8ks5HPJ+3RiSSQK86dYVKnEsk1lsbIHlIRbhSB\n" +
            "QsvcMXy+w2TT7udYINBwwzJPw1YxNv7c6pqEz3/QbbBf4YVf2w+gqM+ioz/8\n" +
            "R1KQzWfTo+BMERMZgbaOd2mo9ogrScYtZofaPwh7fMDt5qP4v9CTov9yB0u/\n" +
            "FAfBCJ+qupTgwCXKvYayyBsyBR/o/d1dCIbbJc9cgn+CJkrsfSUEBU0MGOOs\n" +
            "REq3G/34YmmrkkMfjOIf1iNXvRP2B5jHT2mWi8C27VwcmkmF6plf3kYE1k7t\n" +
            "cs9DKKiqQCUYQC+JcoM1VrXz3c6ebf7AAksMy6c13XjolNXaYGkVOIH7ywyC\n" +
            "gDS1z3P8tGxfjsawqblufjpdk56UQuElkmSN8LE9aUD7lBd7aCG81kkuwEE3\n" +
            "nUw0SieAWsL90+pb8vw2FXpRHs4A1Di7Tl9KkenTWsdO0ueyOnBjz1DYpqOs\n" +
            "qIGOTftDnus4dG1fn07RBW4whLA/y1xXbN/XP4/qQul+JocJn3OjmxheLhMh\n" +
            "9eh6inJm11NwsGS3F27WVbkmv53hJnNbFF80ZXnzvzs3dkMdREHsz9aAff0U\n" +
            "K9aI0LRizHBB8Z6oasRclKIN7/rQ0OIsxuuz/dWeyEbSweoBhpQrpGOCwVBe\n" +
            "tTUJ/DbfY1sywcpPjXsjqCqG2H7O0WD4Pg9PXOfWkWOMyuJNEljbf3B3jYzS\n" +
            "pBihL8AJ+F0xF3jqgGXYEPrwH8UfqwzLu4hrn+8SJgDHMHgtd0xkqFoqyDPc\n" +
            "hn2pNHgGstC6msvsoTfbrHW/IY9PKJyoBPxAYPg+MJUp1Fze+1TPf+40fY81\n" +
            "V/OoY3ELyNVS3fxiXUtyiPAl6S3ZO7hKgWG+j73mlFhpMzfMoINGWmYKbqCP\n" +
            "lg+3R5LdmFU9TzGu2WeXw+tREwpraBkgZardOIzVR3Qlmu030fY8jTm/aAlU\n" +
            "ywLjroik9H9DH1vGH2P98kpvUwfG6gr1QqyHA8oWOu6qMrmH7piZ6svcv2zR\n" +
            "PUrO7olj/qfILDXePo/bcoa0Rr5A/gQIHqFPDZSjMUT6QHVMyYCrm5XFaxMX\n" +
            "qBXxEqU9yq+tTqMGZYDzbliNmQY2Np+Fr60MTVxukIazAYr7yh8Rs+2x3BEa\n" +
            "D58sP6x08DOrerBzVxRtLM3m8vZ4DztF2znGQ2szK2H+b3naZFoNo2rlX3sZ\n" +
            "IG/+h6v+Fs1Qq3VnSeZM1DMeCGmPwUXH0yttZ8b6Z2C+H05hPUj9OrG40Ktm\n" +
            "XMXgSjzlZ+vsafLsWPsNsPjjIoM5KIhXpyyqsDiOJHK2oRf2gEXsg8XskOzJ\n" +
            "hV9o7QYOwNhMkYjmk8G8hLYKdHhDSX8ni6r2k6RJXj0q66KpqgvM9rBUyH8B\n" +
            "VfbxcBXuJpR623nwMNurg34CIbv0bckh6S//A0abyVW9mckK+aOSmXLUQZsG\n" +
            "gP5RAgVQ2IpSiK5pD6neXCr3aJpUHUGygOdEhbtcdKkIPMzz+kaIMFIFXTSz\n" +
            "XEAyUnGYQDqeAZbIcTIlewlPKbxPXzlkGieS3XthXUVF49fXQWNwdErO\n" +
            "=7fbn\n" +
            "-----END PGP MESSAGE-----\n";

    private static final SecureRandom SECURE_RANDOM;
    static {
        try {
            SECURE_RANDOM = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not initialize a strong secure random instance", e);
        }
    }

    private GenerateKeyUtil openPgp;

    @BeforeAll
    public void setUp() {
        openPgp = new GenerateKeyUtil(SECURE_RANDOM);
    }

    @Test
    public void generateJavaKeys() throws PGPException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

//        GenerateKeyUtil.ArmoredKeyPair armoredKeyPairOne = openPgp
//                .generateKeys(4096, "name", "email", "pass");
//
//        System.out.println(armoredKeyPairOne.privateKey());
//        System.out.println(armoredKeyPairOne.publicKey());
//
//        GenerateKeyUtil.ArmoredKeyPair armoredKeyPairTwo = openPgp
//                .generateKeys(4096, "nameTwo", "emailTwo", "passTwo");
//
//        String encryptedMessage = openPgp.encryptAndSign(
//                "wisniaposysa",
//                "email",
//                "pass",
//                armoredKeyPairOne.of(armoredKeyPairOne.privateKey(), armoredKeyPairOne.publicKey()),
//                "emailTwo",
//                armoredKeyPairTwo.publicKey());

        String wiad = openPgp.decryptAndVerify(
                msg,
                "pass",
                GenerateKeyUtil.ArmoredKeyPair.of(privMy, pubThey)
                );
        System.out.println(wiad);
        //System.out.println(armoredKeyPairTwo.privateKey());
        //System.out.println(encryptedMessage);
    }

}