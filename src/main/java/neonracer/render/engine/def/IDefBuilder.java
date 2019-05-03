package neonracer.render.engine.def;

public interface IDefBuilder<I, O extends ObjectDef> {

    O build(I obj);

}
