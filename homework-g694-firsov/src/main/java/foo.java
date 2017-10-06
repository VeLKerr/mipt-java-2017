import ru.mipt.java2017.homework.base.task1.Calculator;
import ru.mipt.java2017.homework.base.task1.ParsingException;

import java.util.ArrayList;

public class  foo implements Calculator{
    @Override
    public double calculate(String s) throws ParsingException {
        if(s==null)
            throw new ParsingException("wrong expression");
        return calculate(s,0,s.length());
    }
    private double calculate(String s, int from,int to) throws ParsingException{
        /*
            x=(' '+'\n'+'\t')*

            S->x+xEx
            S->x-xEx
            S->xEx

            E->(xEx)x
            E->Ex+xEx
            E->Ex-xEx
            E->Ex*xEx
            E->Ex/xEx
            E->Nx

            N->Dx
            N->Dx.xDx
            N->Dx.x
            N->.xDx

            D->DxDx
            D->[0-9]

            E=(+-e)xNx(axNx)^*
         */
        if(from==to)
            throw new ParsingException("wrong expression");
        ArrayList<Double> terms = new ArrayList<>();
        terms.add(0.0);
        Double next_term=1.0;
        Double next_number=0.0;
        Boolean is_last_mult=true;
        Boolean no_number=false;
        //parsin S
        //first x
        while (((s.charAt(from)==' ')||(s.charAt(from)=='\t')||(s.charAt(from)=='\n'))) {
            from++;
            if(from==to)
                throw new ParsingException("wrong expression");
        }
        //parsing + - or e
        if(s.charAt(from)=='+')
            from++;
        else
        if(s.charAt(from)=='-'){
            next_term=-1.0;
            from++;
        }
        //parsing x
        while (((s.charAt(from)==' ')||(s.charAt(from)=='\t')||(s.charAt(from)=='\n'))) {
            from++;
            if(from==to)
                throw new ParsingException("wrong expression");
        }

        //parsing (Nxax)^*
        while (from<to){
            //parsing N
            if(s.charAt(from)=='-'){
                next_term=-1.0;
                from++;
            }
            //parsing x
            while (((s.charAt(from)==' ')||(s.charAt(from)=='\t')||(s.charAt(from)=='\n'))) {
                from++;
                if(from==to)
                    throw new ParsingException("wrong expression");
            }
            if(s.charAt(from)=='('){
                int start=from+1;
                int balance=1;
                while(balance>0){
                    from++;
                    if (from==to) {
                        throw new ParsingException("wrong expression");
                    }
                    if(s.charAt(from)=='(')
                        balance++;
                    if(s.charAt(from)==')')
                        balance--;
                }
                next_number=calculate(s,start,from);
                from++;
                if(from<to)
                    while (((s.charAt(from)==' ')||(s.charAt(from)=='\t')||(s.charAt(from)=='\n'))) {
                        from++;
                        if(from==to)
                            break;
                }
            } else
            if (((s.charAt(from)>='0')&&(s.charAt(from)<='9'))||s.charAt(from)=='.') {
                while ((((s.charAt(from) >= '0') && (s.charAt(from) <= '9'))||(((s.charAt(from)==' ')||(s.charAt(from)=='\t')||(s.charAt(from)=='\n')))) && (from < to)) {
                    if((s.charAt(from)!=' ')&&(s.charAt(from)!='\n')&&(s.charAt(from)!='\t')) {
                        no_number=false;
                        next_number = next_number * 10 + s.charAt(from) - '0';
                    }
                    from++;
                    if(from==to)
                        break;
                }
                if(from<to)
                    if(s.charAt(from)=='.'){
                        Double dot=0.1;
                        from++;
                        while (((s.charAt(from)==' ')||(s.charAt(from)=='\t')||(s.charAt(from)=='\n')))
                            from++;

                        while ((((s.charAt(from) >= '0') && (s.charAt(from) <= '9') )||(((s.charAt(from)==' ')||(s.charAt(from)=='\t')||(s.charAt(from)=='\n'))))&& (from < to)) {
                            if((s.charAt(from)!=' ')&&(s.charAt(from)!='\n')&&(s.charAt(from)!='\t')) {
                                next_number = next_number + (dot * (s.charAt(from) - '0'));
                                no_number=false;
                                dot = dot / 10;
                            }
                            from++;
                            if(from==to)
                                break;
                        }
                    }
                if(no_number)
                    throw new ParsingException("wrong expression");
                no_number=true;
            }else {
                throw new ParsingException("wrong expression");
            }
            //nuber parsed. moving to term
            if(is_last_mult)
                next_term=next_term*next_number;
            else
                //if(next_number!=0)
                next_term=next_term/next_number;
            //else
            //    throw new ParsingException("division on zero");
            next_number=0.0;
            //here we can end
            if(from==to){
                for (int i=0;i<terms.size();i++){
                    next_term=next_term+terms.get(i);
                }
                return next_term;
            }

            //parsing action
            switch (s.charAt(from)){
                case '+':
                    terms.add(next_term);
                    next_term=1.0;
                    is_last_mult=true;
                    break;
                case '-':
                    terms.add(next_term);
                    next_term=-1.0;
                    is_last_mult=true;
                    break;
                case '*':
                    is_last_mult=true;
                    break;
                case '/':
                    is_last_mult=false;
                    break;
                default:
                    throw new ParsingException("wrong expression");
            }
            from++;

            //parsing x
            while (((s.charAt(from)==' ')||(s.charAt(from)=='\t')||(s.charAt(from)=='\n')))
                from++;

        }
        throw new ParsingException("wrong expression");
    }

}

