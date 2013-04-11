/**
 *  @file       CreepyBellElement.sc
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

/**
 *  @class  Plays a bell sound but makes it creepy by playing it re-pitched
 *  and in reverse.
 **/
CreepyBellElement : SoundscapeElement {

  init {
    arg args;

    super.init(args);

    this.outChannel.newPreSend(
      this.soundscape.reverbReturn,
      -10.0.dbamp()
    );

    this.outChannel.level = -20.0.dbamp();

    this.offTimeMin = 12.0;
    this.offTimeMax = 30.0;
    /*this.offTimeMin = 5.0;
    this.offTimeMax = 5.0;*/

    this.transitionTime = 0.1;
  }

  create_next_patch {
    var buf, playbackRate, dur, startTime;

    super.create_next_patch();

    "prepare next bell".postln();

    buf = this.soundscape.bufs[\shipsBellBuf];
    playbackRate = [2.0, -2.0, 1.0, -1.0, 0.25].choose();

    if (playbackRate < 0, {
      startTime = buf.duration - 0.01;
    }, {
      startTime = 0;
    });

    dur = (buf.duration / playbackRate).abs();

    this.onTimeMin = dur;
    this.onTimeMax = dur;

    ^Patch("cs.sfx.PlayBuf", (
      buf: buf,
      attackTime: this.transitionTime,
      releaseTime: this.transitionTime,
      gate: KrNumberEditor.new(0, \gate.asSpec()),
      playbackRate: playbackRate,
      startTime: startTime
    ));
  }
}
