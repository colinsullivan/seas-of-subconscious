/**
 *  @file       SoundsOfSubconsciousSeas.sc
 *
 *  @author     Colin Sullivan <colin [at] colin-sullivan.net>
 *
 *              Copyright (c) 2013 Colin Sullivan
 *              Licensed under the MIT license.
 **/

/**
 *  @class  Entry-point into soundscape.  Creates all elements and plays them.
 *  TODO: generalize this into a `Soundscape` class.
 **/
SoundsOfSubconsciousSeas : Soundscape {

  var <>reverbReturn;

  init_channels {
    super.init_channels();

    this.masterChannel.playfx(FxPatch("cs.fx.Notcher.notcher", (
      notchFreqs: [24000.0 ],
      notchrQs:   [0.1   ],
      notchdBs:   [-6.0  ],
      numChan: 2
    )));

    this.reverbReturn = MixerChannel.new(
      \reverbReturn,
      Server.default,
      2,
      2,
      1.0,
      outbus: this.masterChannel
    );
    this.reverbReturn.playfx(FxPatch("cs.fx.Reverb.reverberator", (
      numChan: 2
    )));


  }

  init_elements {
    super.init_elements();

    this.elements = (
      ambience: AmbienceElement.new(),
      animals: AnimalsSoundscapeElement.new(),
      creaks: CreakSoundscapeElement.new(),
      creepyBells: CreepyBellElement.new(),
      dreams: DreamsSoundscapeElement.new(),
      melodic: MelodicSoundscapeElement.new(),
      fogHorn: FogHornElement.new(),
      shanties: ShantiesSoundscapeElement.new()
    );
  
  }

}
