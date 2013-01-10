CreepyBellElement : SoundscapeElement {

  init {
    arg args;

    super.init(args);

    this.outChannel.newPreSend(
      this.soundscape.reverbReturn,
      -15.0.dbamp()
    );

    this.outChannel.level = -10.0.dbamp();

    /*this.offTimeMin = 10.0;
    this.offTimeMax = 25.0;*/
    this.offTimeMin = 5.0;
    this.offTimeMax = 5.0;

    this.transitionTime = 0.1;
  }

  create_next_patch {
    var buf, rate, dur, startTime;

    super.create_next_patch();

    "prepare next bell".postln();

    buf = this.soundscape.bufs[\shipsBellBuf];
    rate = [2.0, -2.0, 1.0, -1.0, 0.25].choose();

    "rate:".postln;
    rate.postln;

    dur = (buf.duration() * rate).abs();

    this.onTimeMin = dur;
    this.onTimeMax = dur;

    ^Patch("cs.sfx.PlayBuf", (
      buf: buf,
      attackTime: this.transitionTime,
      releaseTime: this.transitionTime,
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      rate: rate
    ));
  }
}
